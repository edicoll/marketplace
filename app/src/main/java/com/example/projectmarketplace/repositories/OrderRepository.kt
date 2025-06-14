package com.example.projectmarketplace.repositories

import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.data.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class OrderRepository {
    private val database: FirebaseFirestore = Firebase.firestore
    private val orderCollection = database.collection("orders")
    private val reviewCollection = database.collection("reviews")
    private val userCollection = database.collection("users")
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

    suspend fun setReview(sellerId: String, rating: Int, comment: String){

        val userId = auth.currentUser?.uid
        val userNameFrom = getUserName(userId.toString())
        setupUserRating(sellerId, rating)

        try {
            val reviewId = reviewCollection.document().id

            val review = Review(
                id = reviewId,
                userIdTo = sellerId,
                userIdFrom = userId.toString(),
                userNameFrom = userNameFrom.toString(),
                rating = rating,
                comment = comment
            )

            reviewCollection.document(reviewId)
                .set(review)
                .await()

        }catch (e: Exception){

        }
    }

    suspend fun setupUserRating(sellerId: String, rating: Int){

        val user = userCollection.document(sellerId)

        val snapshot = user.get().await()
        val currentRating = snapshot.getDouble("rating") ?: 0.0
        val currentCount = snapshot.getLong("ratingCount")?.toInt() ?: 0

        val newSum = currentRating * currentCount + rating
        val newRatingCount = currentCount + 1
        val newRating = newSum / newRatingCount

        user.update(
            "rating", newRating,
            "ratingCount", newRatingCount
        )
    }

    suspend fun getUserName(sellerId: String): String? {
        return try {
            val document = userCollection.document(sellerId).get().await()
            document.getString("name")
        } catch (e: Exception) {
            null
        }
    }
}