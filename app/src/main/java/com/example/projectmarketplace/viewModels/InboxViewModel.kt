package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Conversation
import com.example.projectmarketplace.repositories.ConversationRepository

class InboxViewModel(private val repository: ConversationRepository) : ViewModel() {

    private var _conversations = listOf<Conversation>()
    val conversations: List<Conversation> get() = _conversations //svi conversationi se ovdje spremaju, to je kao getter za _conversations

    suspend fun getConversations(): List<Conversation>{
        if (_conversations.isEmpty()) {                   //da se ne događa ponovno učitavanje bezveze
            _conversations = repository.getConversations()
        }
        return _conversations
    }
}