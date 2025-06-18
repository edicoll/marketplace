package com.example.projectmarketplace.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ProfileRepository {
    private val database: FirebaseFirestore = Firebase.firestore
    private val usersCollection = database.collection("users")
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    suspend fun getUserRating(): Float? {
        val userId = auth.currentUser?.uid

        return try {
            val document = usersCollection.document(userId.toString()).get().await()
            document.getDouble("rating")?.toFloat()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteAccount(): Boolean{
        val userId = auth.currentUser?.uid
        return try {
            usersCollection.document(userId.toString()).delete().await()
            user?.delete()?.await()
            auth.signOut()

            true
        }catch (e: Exception){
            false
        }
    }

}