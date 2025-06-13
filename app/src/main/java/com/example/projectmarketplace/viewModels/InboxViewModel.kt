package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Conversation
import com.example.projectmarketplace.repositories.ConversationRepository

class InboxViewModel(private val repository: ConversationRepository) : ViewModel() {

    private var _conversations = listOf<Conversation>()

    // ne koristim logiku koja je bez ponovnog učitavanja, jer mi se treba učitati lastMessage svaki put
    suspend fun getConversations(): List<Conversation>{

        _conversations = repository.getConversations()
        return _conversations
    }
}