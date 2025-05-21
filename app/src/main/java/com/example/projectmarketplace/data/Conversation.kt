package com.example.projectmarketplace.data
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Conversation(
    val id: Int,
    val participant1Id: Int,
    val participant2Id: Int,
    val participant1Name: String,
    val participant2Name: String,
    val lastMessage: String,
    val timestamp: Long,
    val unreadCount: Int = 0
): Parcelable
