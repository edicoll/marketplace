package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.repositories.ProfileRepository

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    suspend fun getUserRating(): Float? {
        return repository.getUserRating()
    }

    suspend fun deleteAccount(): Boolean{
            return repository.deleteAccount()
    }

}