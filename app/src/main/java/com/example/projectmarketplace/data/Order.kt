package com.example.projectmarketplace.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Order(
    val id: String,
    val sellerId: String,
    val buyerId: String,
    val title: String,
    val category: String,
    val price: Double,
    val orderDate: Date,
    val imageUrl: String
) : Parcelable{
    constructor() : this("", "", "", "", "", 0.0, Date(), "")
}