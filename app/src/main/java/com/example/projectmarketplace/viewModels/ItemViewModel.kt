package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.repositories.ConversationRepository

class ItemViewModel(private val repository: ConversationRepository) : ViewModel() {

    suspend fun createOrGetConversation(currentUserId: String, sellerId: String,): String {

        //provjera ako postoji conversation
        val existingConversation = repository.findConversation(currentUserId, sellerId)

        //ako ne postoji conversation, stvori novi
        return existingConversation ?: repository.createConversation(
            participant1Id = currentUserId,
            participant2Id = sellerId,
            )

    }
}