package com.example.projectmarketplace.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavItem(
    val id: Int,
    val item: Item
) : Parcelable
