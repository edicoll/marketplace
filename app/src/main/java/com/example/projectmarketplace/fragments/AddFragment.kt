package com.example.projectmarketplace.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectmarketplace.databinding.FragmentAddBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.views.AddView

class AddFragment : BaseFragment<FragmentAddBinding>() {

    val categories = listOf("Electronics", "Accessories", "Vehicles")
    val conditions = listOf("New", "Like new", "Used")
    private lateinit var addView: AddView


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddBinding {
        return FragmentAddBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view,savedInstanceState)
        addView = AddView(binding, requireContext())

        addView.setupCategoryDropdown(categories)
        addView.setupConditionDropdown(conditions)

        binding.saveButton.setOnClickListener {
            addView.handleSaveButtonClick()
        }
    }

}

