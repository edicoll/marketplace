package com.example.projectmarketplace.repositories

import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.data.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Date

class OrderRepository {
    private val database: FirebaseFirestore = Firebase.firestore
    private val orderCollection = database.collection("orders")
    private val auth = FirebaseAuth.getInstance()


    suspend fun fetchOrders(): List<Order>{

        val userId = auth.currentUser?.uid

        return try {
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