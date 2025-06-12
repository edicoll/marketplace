package com.example.projectmarketplace.data
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Conversation(
    var id: String,
    val participant1Id: String,
    val participant2Id: String,
    val participant1Name: String,
    val participant2Name: String,
    val lastMessage: String,
    val timestamp: Long,
    val unreadCount: Int = 0
): Parcelable {
    constructor() : this("", "", "", "", "", "", 0L, 0)
}
