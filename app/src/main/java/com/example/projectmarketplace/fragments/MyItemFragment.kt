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
import com.example.projectmarketplace.databinding.FragmentMyitemBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.viewModels.MyItemViewModel
import com.example.projectmarketplace.views.MyItemView
import kotlinx.coroutines.launch

class MyItemFragment : BaseFragment<FragmentMyitemBinding>() {

    private lateinit var viewModel: MyItemViewModel
    private lateinit var myItemView: MyItemView

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyitemBinding {
        return FragmentMyitemBinding.inflate(inflater, container, false)
    }

    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInit()

        myItemView = MyItemView(binding, requireContext(), requireActivity(), viewModel)

        setupBackButton(binding.back)

        myItemView.setupRecyclerView()

        lifecycleScope.launch {
            myItemView.fetchMyItems()
        }

    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MyItemViewModel(ItemRepository()) as T
            }
        }).get(MyItemViewModel::class.java)
    }

}