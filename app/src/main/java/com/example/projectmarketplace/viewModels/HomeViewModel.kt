package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.repositories.ItemRepository


class HomeViewModel(private val repository: ItemRepository) : ViewModel() {

    suspend fun getItemsExcludingCurrentUser(): List<Item> {

        return repository.getItemsExcludingCurrentUser()
    }
}