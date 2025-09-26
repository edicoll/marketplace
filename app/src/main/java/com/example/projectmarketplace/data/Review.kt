package com.example.projectmarketplace.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Review(
    val id: String,
    val userIdTo: String,
    val userIdFrom: String,
    val userNameFrom: String,
    val rating: Int,
    val comment: String,
    val itemName: String,
    val imageUrl: String,
    val createdAt: Date
    ) : Parcelable{

    constructor() : this("", "", "", "", 0, "", "", "", Date())
}
