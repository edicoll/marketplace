package com.example.projectmarketplace.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Item(
    val title: String,
    val description: String,
    val price: Double,
    val brand: String,
    val condition: String,
    val sellerId: String,
    val color: String,
    val createdAt: Date,
    val category: String,

    ): Parcelable{
    constructor() : this("", "", 0.0, "", "", "", "", Date(), "")
}
