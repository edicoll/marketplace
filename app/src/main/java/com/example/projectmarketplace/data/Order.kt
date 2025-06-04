package com.example.projectmarketplace.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val id: Int,
    val buyerId: Int,
    val buyerName: String,
    val item: Item,
    val orderDate: Long
) : Parcelable