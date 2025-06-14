package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.repositories.ItemRepository

class ProfileViewModel(private val repository: ItemRepository) : ViewModel() {

    suspend fun getUserRating(): Float? {
        return repository.getUserRating()
    }

}