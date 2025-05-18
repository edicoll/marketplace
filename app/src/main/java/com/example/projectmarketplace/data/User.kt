package com.example.projectmarketplace.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val email: String,
    var rating: Float,
    val password: String
) : Parcelable
