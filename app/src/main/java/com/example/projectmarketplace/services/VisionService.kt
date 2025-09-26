package com.example.projectmarketplace.services

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.tasks.await
import java.util.Locale

class VisionService(context: Context) {

    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    suspend fun getImageTags(bitmap: Bitmap): List<String> {
        return try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val labels = labeler.process(image).await()

            labels.map { label ->
                label.text.toLowerCase(Locale.ROOT).trim()
            }.filter { tag ->
                tag.length > 3 && !tag.contains(" ")
            }.distinct()

        } catch (e: Exception) {
            emptyList()
        }
    }
}