package com.example.projectmarketplace.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Message
import com.example.projectmarketplace.repositories.MessageRepository

class InboxIndividualViewModel(private val repository: MessageRepository) : ViewModel() {


    private var _messages = listOf<Message>()
    val messages: List<Message> get() = _messages

    suspend fun getMessages(conversationId: String): List<Message>{

        if (_messages.isEmpty()) {
            _messages = repository.getMessages(conversationId)
        }

        return _messages
    }

    suspend fun sendMessage(conversationId: String, senderId: String, text: String): Boolean {
        return repository.sendMessage(conversationId, senderId, text)
    }

    fun startListening(conversationId: String, onUpdate: (List<Message>) -> Unit) {
        repository.listenForMessages(conversationId) { messages ->   //osluškuje i dobiva nove poruke
            _messages = messages                            //kada se dobiju nove poruke, ažurira se messages
            onUpdate(messages)                              //poziva callback funkciju
        }
    }
}