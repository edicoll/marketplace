package com.example.projectmarketplace.repositories

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
    private val auth = FirebaseAuth.getInstance()

    suspend fun addItem(item: Item): String {  //suspend služi da ta funkcija može biti pauzirana i nastaviti se kasnije, bez ometanja  glavnog threada
        val ref = itemsCollection.add(item).await() //await kao pretvara firebase task u kotlin rezultat
        return ref.id
    }


    suspend fun getItemsExcludingCurrentUser(): List<Item> {
        val currentUserId = auth.currentUser?.uid ?: return emptyList()

        return try {
            val querySnapshot = database.collection("items")
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
            val document = database.collection("users").document(sellerId).get().await()
            document.getString("name")
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getSellerRating(sellerId: String): Float? {
        return try {
            val document = database.collection("users").document(sellerId).get().await()
            document.getDouble("rating")?.toFloat()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserRating(): Float? {
        val userId = auth.currentUser?.uid

        return try {
            val document = database.collection("users").document(userId.toString()).get().await()
            document.getDouble("rating")?.toFloat()
        } catch (e: Exception) {
            null
        }
    }

    /*  za sad ami ne koristi
    suspend fun getItemsByUser(userId: String): List<Item> {
        val querySnapshot = itemsCollection
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return querySnapshot.documents.map { doc ->
            doc.toObject(Item::class.java)?.copy(id = doc.id) ?: Item()
        }
    }*/
    /* za sada ništa
    suspend fun updateItem(item: Item) {
        itemsCollection.document(item.id).set(item).await()
    }*/

    suspend fun deleteItem(itemId: String) {
        itemsCollection.document(itemId).delete().await()
    }

    suspend fun getFavItems():List<Item> {
        return try {

            val userId = auth.currentUser?.uid

            val userDoc = database.collection("users").document(userId.toString())
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
}