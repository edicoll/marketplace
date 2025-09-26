package com.example.projectmarketplace.repositories

import android.net.Uri
import com.example.projectmarketplace.data.Item
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.Date

class AddRepository {
    private val database: FirebaseFirestore = Firebase.firestore
    private val itemsCollection = database.collection("items")
    private val auth = FirebaseAuth.getInstance()
    private val storage = Firebase.storage

    suspend fun addItem(
        title: String, description: String, price: Float, category: String,
        condition: String, brand: String, color: String, imageUri: Uri?,
        latitude: Double?, longitude: Double?
    ): Boolean{
        return try {

            val imageUrl = imageUri?.let { uri ->
                val storageRef = storage.reference
                val imageRef = storageRef.child("item_images/${System.currentTimeMillis()}.jpg")
                imageRef.putFile(uri).await()
                imageRef.downloadUrl.await().toString()
            }


            val geohash = if (latitude != null && longitude != null) {
                GeoFireUtils.getGeoHashForLocation(GeoLocation(latitude, longitude))
            } else {
                null
            }

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
                category = category,
                imageUrl = imageUrl.toString(),
                latitude = latitude,
                longitude = longitude,
                geohash = geohash
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