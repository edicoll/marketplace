package com.example.projectmarketplace.repositories

import com.example.projectmarketplace.data.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ReviewRepository {
    private val database: FirebaseFirestore = Firebase.firestore
    private val reviewCollection = database.collection("reviews")
    private val userCollection = database.collection("users")
    private val auth = FirebaseAuth.getInstance()

    suspend fun fetchReviews(): List<Review>{

        val userId = auth.currentUser?.uid

        return try {
            val reviews = reviewCollection
                .whereEqualTo("userIdTo", userId)
                .get()
                .await()
                .toObjects(Review::class.java)

            reviews
        }catch (e: Exception){
            emptyList<Review>()
        }
    }

    suspend fun getUsername(): String{
        val userId = auth.currentUser?.uid

        return try {
            val document = userCollection.document(userId.toString()).get().await()
            document.getString("name")
        } catch (e: Exception) {
            null
        }.toString()
    }


}