package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.repositories.ItemRepository
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel(private val repository: ItemRepository) : ViewModel() {

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    fun getItemsExcludingCurrentUser(
        onSuccess: (List<Item>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val items = repository.getItemsExcludingCurrentUser()
                onSuccess(items)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}