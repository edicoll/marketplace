package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.repositories.ItemRepository

class SearchViewModel(private val repository: ItemRepository) : ViewModel() {
    private var _originalItems = listOf<Item>()
    val originalItems: List<Item> get() = _originalItems //podatci searcha se ovdje spremaju, to je kao getter za _originalItems

    suspend fun getItems(): List<Item> {
        _originalItems = repository.getItems()
        return _originalItems
    }
}