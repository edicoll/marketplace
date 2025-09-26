package com.example.projectmarketplace.services

import android.Manifest
import androidx.annotation.RequiresPermission
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.repositories.UserRepository
import com.example.projectmarketplace.utils.LocationUtils
import com.google.android.gms.maps.model.LatLng
import android.util.Log
import javax.inject.Inject

class RecommendationService @Inject constructor(
    private val itemRepository: ItemRepository,
    private val userRepository: UserRepository,
    private val locationService: LocationService
) {


    suspend fun getRecommendedItems(userId: String): List<Item> {
        // svi itemi osim korisnikovih
        val allItems = itemRepository.getItemsExcludingCurrentUser()

        //lokacija usera
        val userLocation = locationService.getCurrentUserLocation()

        // favoriti i narudžbe korisnika
        val userFavorites = userRepository.getUserFavorites(userId)
        val userPurchases = userRepository.getUserPurchases(userId)

        // ako nema podataka, uzimaju se popularni artikli
        if (userFavorites.isEmpty() && userPurchases.isEmpty()) {
            return getPopularItems(allItems)
        }

        return generateRecommendations(allItems, userFavorites, userPurchases, userLocation)
    }

    private fun generateRecommendations(
        allItems: List<Item>,
        favorites: List<Item>,
        purchases: List<Order>,
        userLocation: LatLng?
    ): List<Item> {




        return allItems
            .filter { item ->
                // isključuju se favoriti
                !favorites.any { it.id == item.id }
            }
            .sortedByDescending { item ->
                calculateRecommendationScore(item, favorites, purchases, userLocation)
            }
            .take(6) // uzima se 6 preporuka za početak
    }

    private fun calculateRecommendationScore(
        item: Item,
        favorites: List<Item>,
        purchases: List<Order>,
        userLocation: LatLng?
    ): Int {
        var score = 0

        // 1. prvi pokazatelj je kategorija
        favorites.forEach { favorite ->
            if (favorite.category == item.category) score += 3
        // drugi pokazatelj brend
            if (favorite.brand == item.brand) score += 2
        }
        purchases.forEach { purchase ->
            if (purchase.category == item.category) score += 5
        }


        // 3. pokazatelj lokacija
        if (userLocation != null){

            if (item.latitude != null && item.longitude != null) {
                val distance = calculateDistance(
                    userLocation.latitude, userLocation.longitude,
                    item.latitude, item.longitude
                )
                if (distance < 20) score += 10
            }

        }
        Log.d("item", "item name: ${item.title}, score: $score")
        return score
    }

    private fun getPopularItems(items: List<Item>): List<Item> {
        // vraća random
        return items.shuffled().take(6)
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return LocationUtils.calculateDistance(lat1, lon1, lat2, lon2)
    }
}