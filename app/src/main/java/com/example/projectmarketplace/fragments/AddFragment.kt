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
import android.net.Uri
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class AddFragment : BaseFragment<FragmentAddBinding>() {

    val categories = listOf("Electronics", "Accessories", "Vehicles")
    val conditions = listOf("New", "Like new", "Used")
    private lateinit var addView: AddView
    private lateinit var viewModel: AddViewModel
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.imagePreview.setImageURI(it)
            binding.removeImageButton.visibility = View.VISIBLE
            binding.selectImageButton.visibility = View.GONE
        }
    }


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

        binding.selectImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.removeImageButton.setOnClickListener {
            removeSelectedImage()
        }

        binding.saveButton.setOnClickListener {
            addView.handleSaveButtonClick(selectedImageUri)
        }
    }

    private fun removeSelectedImage() {
        selectedImageUri = null
        binding.imagePreview.setImageResource(android.R.drawable.ic_menu_gallery)
        binding.removeImageButton.visibility = View.GONE
        binding.selectImageButton.visibility = View.VISIBLE
    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AddViewModel(AddRepository()) as T
            }
        }).get(AddViewModel::class.java)
    }

}

