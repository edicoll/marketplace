package com.example.projectmarketplace.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.repositories.AddRepository



class AddViewModel(private val repository: AddRepository) : ViewModel() {

    suspend fun addItem(
        title: String, description: String, price: Float, category: String,
        condition: String, brand: String, color: String, imageUri: Uri?,
        latitude: Double?, longitude: Double?
    ): Boolean{

        return repository.addItem(title, description, price, category, condition,
            brand, color, imageUri, latitude, longitude)

    }
}
