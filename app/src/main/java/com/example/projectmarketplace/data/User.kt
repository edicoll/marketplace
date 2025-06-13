package com.example.projectmarketplace.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val email: String,
    var rating: Float,
    val favItems: List<String> = emptyList()
) : Parcelable
