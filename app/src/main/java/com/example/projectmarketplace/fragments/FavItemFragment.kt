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
import com.example.projectmarketplace.databinding.FragmentFavitemBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.viewModels.FavItemViewModel
import com.example.projectmarketplace.views.FavItemView
import kotlinx.coroutines.launch

class FavItemFragment : BaseFragment<FragmentFavitemBinding>() {

    private lateinit var viewModel: FavItemViewModel
    private lateinit var favItemView: FavItemView

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavitemBinding {
        return FragmentFavitemBinding.inflate(inflater, container, false)
    }

    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInit()

        favItemView = FavItemView(binding, requireContext(), requireActivity(), viewModel)

        setupBackButton(binding.back)

        favItemView.setupRecyclerView()

        lifecycleScope.launch {
            favItemView.fetchFavItems()
        }

    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FavItemViewModel(ItemRepository()) as T
            }
        }).get(FavItemViewModel::class.java)
    }

}