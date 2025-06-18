package com.example.projectmarketplace.repositories

import android.util.Log
import com.example.projectmarketplace.data.Conversation
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Date
import kotlin.text.get

class ConversationRepository {
    private val database: FirebaseFirestore = Firebase.firestore
    private val conversationCollection = database.collection("conversations")
    private val orderCollection = database.collection("orders")
    private val itemCollection = database.collection("items")
    private val userCollection = database.collection("users")
    private val auth = FirebaseAuth.getInstance()


    suspend fun findConversation(user1Id: String, user2Id: String): String? {
        return try {

            // provjeri jednu mogućnost
            val query1 = conversationCollection
                .whereEqualTo("participant1Id", user1Id)
                .whereEqualTo("participant2Id", user2Id)
                .limit(1)
                .get()
                .await()

            if (!query1.isEmpty) {
                return query1.documents[0].id
            }
            // provjeri drugu mogućnost
            val query2 = conversationCollection
                .whereEqualTo("participant1Id", user2Id)
                .whereEqualTo("participant2Id", user1Id)
                .limit(1)
                .get()
                .await()

            if (!query2.isEmpty) {
                return query2.documents[0].id
            }

            null
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun createConversation(
        participant1Id: String,
        participant2Id: String,
    ): String {
        return try {

                //najprije se generira nasumični id
            val conversationId = conversationCollection.document().id

                //onda se taj id postavlja u conversation
            val conversation = Conversation(
                id = conversationId,
                participant1Id = participant1Id,
                participant2Id = participant2Id,
                participant1Name = getUserName(participant1Id).toString(),
                participant2Name = getUserName(participant2Id).toString(),
                lastMessage = "",
                timestamp = System.currentTimeMillis(),
                participant1unreadCount = 0,
                participant2unreadCount = 0
            )

            conversationCollection.document(conversationId)
                .set(conversation)
                .await()

             conversationId

        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUserName(userId: String): String? {
        return try {
            val document = userCollection.document(userId).get().await()
            document.getString("name")
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getConversations(): List<Conversation> {

        return try {
            //  gdje je korisnik participant1
            val query1 = conversationCollection
                .whereEqualTo("participant1Id", auth.currentUser?.uid)
                .get()
                .await()

            //  gdje je korisnik participant2
            val query2 = conversationCollection
                .whereEqualTo("participant2Id", auth.currentUser?.uid)
                .get()
                .await()

            // spoji rezultate
            val combined = query1.documents + query2.documents

            combined.mapNotNull { doc ->
                doc.toObject(Conversation::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun setFavItem(item: Item){
        try {
            val userId = auth.currentUser?.uid
            val userRef = userCollection.document(userId.toString())

            userRef.update("favItems", FieldValue.arrayUnion(item.id)).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun removeFavItem(item: Item) {
        try {
            val userId = auth.currentUser?.uid
            val userRef = userCollection.document(userId.toString())

            userRef.update("favItems", FieldValue.arrayRemove(item.id)).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun isItemFavorite(itemId: String): Boolean {
        return try {
            val userId = auth.currentUser?.uid

            val userRef = userCollection.document(userId.toString())
            val document = userRef.get().await()

            val favItems = document.get("favItems") as? List<*>

            val containsItem = favItems?.any {
                it?.toString() == itemId
            } == true

            containsItem
        } catch (e: Exception) {

            false
        }
    }

    suspend fun buyItem(item: Item): List<Order>{

        val userId = auth.currentUser?.uid

        return try {
            val orderId = orderCollection.document().id

            val order = Order(
                id = orderId,
                sellerId = item.sellerId,
                buyerId = userId.toString(),
                title = item.title,
                price = item.price,
                orderDate = Date()
            )

            orderCollection.document(orderId)
                .set(order)
                .await()

            itemCollection.document(item.id).delete().await()

            val orders = orderCollection
                .whereEqualTo("buyerId", userId)
                .get()
                .await()
                .toObjects(Order::class.java)

            orders
        }catch (e: Exception){
            emptyList<Order>()
        }
    }

}
