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
import com.example.projectmarketplace.databinding.FragmentHomeBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.repositories.UserRepository
import com.example.projectmarketplace.services.LocationService
import com.example.projectmarketplace.viewModels.HomeViewModel
import com.example.projectmarketplace.views.HomeView
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var homeView: HomeView


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInit()

        homeView = HomeView(binding, requireContext(), requireActivity(), viewModel)

        homeView.setupRecyclerView()

        lifecycleScope.launch {
            homeView.fetchItems()
        }
    }


    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(ItemRepository(), UserRepository(), LocationService(requireContext())) as T
            }
        }).get(HomeViewModel::class.java)
    }
}



