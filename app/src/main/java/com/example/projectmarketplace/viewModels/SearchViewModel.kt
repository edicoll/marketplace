package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.repositories.ItemRepository

class SearchViewModel (private val repository: ItemRepository) : ViewModel() {

    suspend fun getItems(): List<Item> {
        return repository.getItems()
    }
}