package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.repositories.ItemRepository

class FavItemViewModel(private val repository: ItemRepository) : ViewModel() {

    suspend fun fetchFavItems(): List<Item>{

        return repository.getFavItems()
    }
}