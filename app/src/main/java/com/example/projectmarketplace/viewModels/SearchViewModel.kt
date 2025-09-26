package com.example.projectmarketplace.viewModels

import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.utils.LocationUtils
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation

class SearchViewModel(private val repository: ItemRepository) : ViewModel() {
    private var _originalItems = listOf<Item>()
    val originalItems: List<Item> get() = _originalItems //podatci searcha se ovdje spremaju, to je kao getter za _originalItems

    suspend fun getItems(): List<Item> {
        _originalItems = repository.getItemsExcludingCurrentUser()
        return _originalItems
    }

    suspend fun getItemsInRadius(centerLat: Double, centerLng: Double, radiusKm: Double): List<Item> {
        return _originalItems.filter { item ->
            item.latitude != null && item.longitude != null &&
                    LocationUtils.calculateDistance(
                        centerLat, centerLng,
                        item.latitude!!, item.longitude!!
                    ) <= radiusKm
        }
    }

    // geohash
    suspend fun getItemsInRadiusWithGeoHash(centerLat: Double, centerLng: Double, radiusKm: Double): List<Item> {
        val center = GeoLocation(centerLat, centerLng)
        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusKm * 1000) // radius u metrima

        val itemsInBounds = bounds.flatMap { bound ->
            // Ovdje implementirajte Firebase query s geohash range-om
            // Ovo je pojednostavljena implementacija
            _originalItems.filter { item ->
                item.geohash != null &&
                        item.geohash!! >= bound.startHash &&
                        item.geohash!! <= bound.endHash
            }
        }

        // filtriraj točno po radijusu
        return itemsInBounds.filter { item ->
            LocationUtils.calculateDistance(
                centerLat, centerLng,
                item.latitude!!, item.longitude!!
            ) <= radiusKm
        }
    }
}