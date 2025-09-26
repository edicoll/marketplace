package com.example.projectmarketplace.viewModels

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.repositories.UserRepository
import com.example.projectmarketplace.services.LocationService
import com.example.projectmarketplace.services.RecommendationService


class HomeViewModel(
    private val repository: ItemRepository,
    private val userRepository: UserRepository,
    private val locationService: LocationService
) : ViewModel() {

    suspend fun getItemsExcludingCurrentUser(): List<Item> {

        return repository.getItemsExcludingCurrentUser()
    }

    suspend fun getRecommendedItems(userId: String): List<Item> {

        val recommendationService = RecommendationService(repository, userRepository, locationService)
        return recommendationService.getRecommendedItems(userId)
    }

    suspend fun getRecentlyViewedItems(): List<Item>{

        return repository.getRecentlyViewedItems()
    }
}