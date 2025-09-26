package com.example.projectmarketplace.data
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Item(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val brand: String,
    val condition: String,
    val sellerId: String,
    val color: String,
    val createdAt: Date,
    val category: String,
    val imageUrl: String,
    val latitude: Double?,
    val longitude: Double?,
    val geohash: String? = null

): Parcelable {
    constructor() : this("", "", "", 0.0, "", "", "", "", Date(), "", "", null, null, null)
}
