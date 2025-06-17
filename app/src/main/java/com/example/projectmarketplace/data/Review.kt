package com.example.projectmarketplace.data
import android.os.Parcelable
import com.example.projectmarketplace.data.Order
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Review(
    val id: String,
    val userIdTo: String,
    val userIdFrom: String,
    val userNameFrom: String,
    val rating: Int,
    val comment: String) : Parcelable{

    constructor() : this("", "", "", "", 0, "")
}
