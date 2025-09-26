package com.example.projectmarketplace.repositories

import android.util.Log
import com.example.projectmarketplace.data.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ItemRepository {
    private val database: FirebaseFirestore = Firebase.firestore
    private val itemsCollection = database.collection("items")
    private val userCollection = database.collection("users")
    private val auth = FirebaseAuth.getInstance()


    suspend fun getItemsExcludingCurrentUser(): List<Item> {
        val currentUserId = auth.currentUser?.uid ?: return emptyList()

        return try {
            val querySnapshot = itemsCollection
                .whereNotEqualTo("sellerId", currentUserId) // filtriranje po sellerId
                .get()
                .await()

            querySnapshot.documents.mapNotNull { doc ->
                doc.toObject(Item::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


    suspend fun getSellerName(sellerId: String): String? {
        return try {
            val document = userCollection.document(sellerId).get().await()
            document.getString("name")
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getSellerRating(sellerId: String): Float? {
        return try {
            val document = userCollection.document(sellerId).get().await()
            document.getDouble("rating")?.toFloat()
        } catch (e: Exception) {
            null
        }
    }


    suspend fun deleteItem(item: Item): Boolean {

        return try {
            itemsCollection.document(item.id).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getFavItems():List<Item> {
        return try {

            val userId = auth.currentUser?.uid

            val userDoc = userCollection.document(userId.toString())
                .get()
                .await()

            val favItemIds = userDoc.get("favItems") as? List<*> ?: return emptyList()

            val items = itemsCollection
                .whereIn(FieldPath.documentId(), favItemIds)
                .get()
                .await()
                .mapNotNull { it.toObject(Item::class.java) }

            items
        } catch (e: Exception) {

            emptyList()
        }
    }

    suspend fun getItemsByCurrentUser(): List<Item> {
        val currentUserId = auth.currentUser?.uid ?: return emptyList()

        return try {
            val querySnapshot = itemsCollection
                .whereEqualTo("sellerId", currentUserId)
                .get()
                .await()

            querySnapshot.documents.mapNotNull { doc ->
                doc.toObject(Item::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateItem(item: Item): Boolean {
        return try {
            val itemData = hashMapOf(
                "title" to item.title,
                "price" to item.price,
                "brand" to item.brand,
                "description" to item.description,
                "condition" to item.condition,
                "color" to item.color,
            )

            itemsCollection.document(item.id).update(itemData.toMap()).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getRecentlyViewedItems(): List<Item> {

        val userId = auth.currentUser?.uid ?: return emptyList()

        return try {

            val userDoc = userCollection.document(userId).get().await()
            val recentlyViewedIds = userDoc.get("recViewedItems") as? List<*> ?: return emptyList()

            if (recentlyViewedIds.isEmpty()) {
                return emptyList()
            }

            val snapshot = itemsCollection
                .whereIn(FieldPath.documentId(), recentlyViewedIds)
                .get()
                .await()

            val itemsMap = snapshot.documents
                .mapNotNull { it.toObject(Item::class.java)?.let { item -> it.id to item } }
                .toMap()

            val sortedItems = recentlyViewedIds.mapNotNull { itemsMap[it] }

            sortedItems.reversed()

        } catch (e: Exception) {
            emptyList()
        }
    }
}