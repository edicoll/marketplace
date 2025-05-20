package com.example.projectmarketplace.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Message(
    val id: Int,
    val senderId: Int,
    val senderName: String,
    val recieverId: Int,
    val text: String,
    val timestamp: Long,
    var isRead: Boolean = false
    ): Parcelable