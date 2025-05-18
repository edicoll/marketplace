package com.example.projectmarketplace.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val id: Int,
    val userIdTo: Int,
    val userIdFrom: Int,
    val rating: Int,
    val comment: String) : Parcelable
