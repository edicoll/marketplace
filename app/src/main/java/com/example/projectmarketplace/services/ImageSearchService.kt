// services/ImageSearchService.kt
package com.example.projectmarketplace.services

import android.graphics.Bitmap
import android.util.Log
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.repositories.ItemRepository
import javax.inject.Inject

class ImageSearchService @Inject constructor(
    private val itemRepository: ItemRepository
) {

    private lateinit var visionService: VisionService

    fun setVisionService(service: VisionService) {
        visionService = service
    }

    suspend fun searchByImage(bitmap: Bitmap): List<Item> {
        return try {
            // 1. dohvaćanje tagova
            val tags = visionService.getImageTags(bitmap)
            Log.d("Tagovi slika", "Detektirani tagovi: $tags")

            if (tags.isEmpty()) {
                Log.d("Tagovi slika", "Nema tagova za pretragu")
                return emptyList()
            }

            val allItems = itemRepository.getItemsExcludingCurrentUser()
            Log.d("Tagovi slika", "Ukupno artikala: ${allItems.size}")

            // 3. filtriraj po tagovima
            val results = allItems.filter { item ->
                tags.any { tag -> isItemMatchingTag(item, tag) }
            }

            Log.d("Tagovi slika", "Pronađeno ${results.size} rezultata")
            results

        } catch (e: Exception) {
            Log.e("Tagovi slika", "Greška u pretrazi", e)
            emptyList()
        }
    }

    private fun isItemMatchingTag(item: Item, tag: String): Boolean {
        return item.title.contains(tag, ignoreCase = true) ||
                item.description.contains(tag, ignoreCase = true) ||
                item.category.contains(tag, ignoreCase = true) ||
                item.brand.contains(tag, ignoreCase = true)
    }
}