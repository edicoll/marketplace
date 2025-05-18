package com.example.projectmarketplace.data
import android.graphics.Color
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: Int,
    val selerId: Int,
    val title: String,
    val description: String,
    val brand: String,
    val condition: String,
    val color: String,
    val price: Float,

): Parcelable
