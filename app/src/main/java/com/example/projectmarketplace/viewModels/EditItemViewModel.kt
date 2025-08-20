package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.repositories.ItemRepository

class EditItemViewModel(private val repository: ItemRepository) : ViewModel() {

    suspend fun deleteItem(item: Item): Boolean{

        return repository.deleteItem(item)
    }

}