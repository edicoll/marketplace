package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.projectmarketplace.databinding.FragmentReviewBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ReviewRepository
import com.example.projectmarketplace.viewModels.ReviewViewModel
import com.example.projectmarketplace.views.ReviewView
import kotlinx.coroutines.launch


class ReviewFragment : BaseFragment<FragmentReviewBinding>() {

    private lateinit var viewModel: ReviewViewModel
    private lateinit var reviewView: ReviewView


    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentReviewBinding {
        return FragmentReviewBinding.inflate(inflater, container, false)
    }


    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInit()

        reviewView = ReviewView(binding, requireContext(), requireActivity(), viewModel)

        setupBackButton(binding.back)


        reviewView.setupRecyclerView()

        lifecycleScope.launch {
            reviewView.setUserName()
            reviewView.fetchReviews()
        }


    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ReviewViewModel(ReviewRepository()) as T
            }
        }).get(ReviewViewModel::class.java)
    }


}