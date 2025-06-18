package com.example.projectmarketplace.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectmarketplace.databinding.FragmentAddBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.AddRepository
import com.example.projectmarketplace.viewModels.AddViewModel
import com.example.projectmarketplace.views.AddView

class AddFragment : BaseFragment<FragmentAddBinding>() {

    val categories = listOf("Electronics", "Accessories", "Vehicles")
    val conditions = listOf("New", "Like new", "Used")
    private lateinit var addView: AddView
    private lateinit var viewModel: AddViewModel


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddBinding {
        return FragmentAddBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view,savedInstanceState)

        viewModelInit()

        addView = AddView(binding, requireContext(), lifecycleOwner = viewLifecycleOwner, viewModel)

        addView.setupCategoryDropdown(categories)
        addView.setupConditionDropdown(conditions)

        binding.saveButton.setOnClickListener {
            addView.handleSaveButtonClick()
        }
    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AddViewModel(AddRepository()) as T
            }
        }).get(AddViewModel::class.java)
    }

}

