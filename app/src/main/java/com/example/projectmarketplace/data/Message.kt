package com.example.projectmarketplace.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Message(
    var id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestamp: Long,
    var isRead: Boolean = false
    ): Parcelable{
        constructor() : this("", "", "", "", "", 0L, false)
    }