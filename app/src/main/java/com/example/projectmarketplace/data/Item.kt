package com.example.projectmarketplace.data
import android.graphics.Color
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: Int,
    val sellerId: Int,
    val sellerName: String,
    val sellerRating: Float,
    val title: String,
    val description: String,
    val category: String,
    val brand: String,
    val condition: String,
    val color: String,
    val price: Float,
    val timestamp: Long,

): Parcelable
