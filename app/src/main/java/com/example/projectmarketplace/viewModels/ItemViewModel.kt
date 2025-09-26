package com.example.projectmarketplace.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.data.Order
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

    suspend fun setFavItem(item: Item){
        repository.setFavItem(item)

    }
    suspend fun removeFavItem(item: Item){
        repository.removeFavItem(item)
    }
    suspend fun isItemFavorite(itemId: String): Boolean{
        return repository.isItemFavorite(itemId)
    }
    suspend fun buyItem(item: Item): List<Order>{
        return repository.buyItem(item)
    }

    suspend fun upgradeRecentlyViewed(itemId: String){
        repository.upgradeRecentlyViewed(itemId)
    }
}

