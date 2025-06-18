package com.example.projectmarketplace.repositories

import com.example.projectmarketplace.data.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Date

class AddRepository {
    private val database: FirebaseFirestore = Firebase.firestore
    private val itemsCollection = database.collection("items")
    private val auth = FirebaseAuth.getInstance()

    suspend fun addItem(title: String, description: String, price: Float, category: String,
                        condition: String, brand: String, color: String): Boolean{
        return try {
            val itemId = itemsCollection.document().id
            val userId = auth.currentUser?.uid

            val newItem = Item(
                id = itemId,
                title = title,
                description = description,
                price = price.toDouble(),
                brand = brand,
                condition = condition,
                sellerId = userId.toString(),
                color = color,
                createdAt = Date(),
                category = category
            )

            itemsCollection.document(itemId)
                .set(newItem)
                .await()

            return true
        }catch (e: Exception){
            return false
        }
    }
}