package com.example.projectmarketplace.viewModels


import androidx.lifecycle.ViewModel
import com.example.projectmarketplace.data.Review
import com.example.projectmarketplace.repositories.ReviewRepository

class ReviewViewModel(private val repository: ReviewRepository) : ViewModel() {
    private var _reviews = listOf<Review>()
    val reviews: List<Review> get() = _reviews


    suspend fun fetchReviews(): List<Review>{
        if (_reviews.isEmpty()) {
            _reviews = repository.fetchReviews()
        }
        return _reviews
    }

    suspend fun getUsername(): String{
        return repository.getUsername()
    }

}