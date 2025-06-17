package com.example.projectmarketplace.views

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.ReviewAdapter
import com.example.projectmarketplace.databinding.FragmentReviewBinding
import com.example.projectmarketplace.viewModels.ReviewViewModel

class ReviewView(private val binding: FragmentReviewBinding,
                 private val context: Context,
                 private val activity: FragmentActivity,
                 private  var viewModel: ReviewViewModel ) {

    private lateinit var adapter: ReviewAdapter
    private lateinit var recyclerView: RecyclerView

    fun setupRecyclerView() {

        recyclerView = binding.reviewRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = ReviewAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    suspend fun setUserName(){
        binding.name.text = viewModel.getUsername()
    }

    suspend fun fetchReviews(){
        viewModel.fetchReviews()
        val reviews = viewModel.reviews

        adapter = ReviewAdapter(reviews)
        recyclerView.adapter = adapter
    }
}