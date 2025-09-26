package com.example.projectmarketplace.repositories

import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.data.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val database: FirebaseFirestore = Firebase.firestore
    private val usersCollection = database.collection("users")
    private val itemsCollection = database.collection("items")
    private val orderCollection = database.collection("orders")
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    suspend fun getUserFavorites(userId: String): List<Item> {
        return try {
            val userDoc = usersCollection.document(userId)
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

    // ako bude potrebno morat ću ordere pretvarat u iteme
    suspend fun getUserPurchases(userId: String): List<Order> {
        return try {
            val orders = orderCollection
                .whereEqualTo("buyerId", userId)
                .get()
                .await()
                .toObjects(Order::class.java)

            orders
        } catch (e: Exception) {
            emptyList()
        }
    }

}