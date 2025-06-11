package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.repositories.ItemRepository


class HomeViewModel(private val repository: ItemRepository) : ViewModel() {
    private var _items = listOf<Item>()
    val items: List<Item> get() = _items //svi itemi se ovdje spremaju, to je kao getter za _originalItems

    //fun hasItems(): Boolean = _items.isNotEmpty()

    suspend fun getItemsExcludingCurrentUser(): List<Item> {
        if (_items.isEmpty()) {                   //da se ne događa ponovno učitavanje bezveze
            _items = repository.getItemsExcludingCurrentUser()
        }
        return _items
    }
}