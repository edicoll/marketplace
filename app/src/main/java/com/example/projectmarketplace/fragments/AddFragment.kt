package com.example.projectmarketplace.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentAddBinding
import com.example.projectmarketplace.fragments.base.BaseFragment

class AddFragment : BaseFragment<FragmentAddBinding>() {

    private val fillInAllFields = "Please fill in all fields."
    private val inputCorrectPrice = "Input correct price."
    private val itemSuccessfullyAdded = "Item successfully added!"
    val categories = listOf("Electronics", "Accessories", "Vehicles")
    val conditions = listOf("New", "Like new", "Used")

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddBinding {
        return FragmentAddBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view,savedInstanceState)

        setupCategoryDropdown()
        setupConditionDropdown()

        binding.saveButton.setOnClickListener {
            handleSaveButtonClick()
        }
    }

    private fun handleSaveButtonClick(){
        //dohvaćanje unesenih vrijednosti
        val title = binding.titleInput.text.toString()
        val description = binding.descriptionInput.text.toString()
        val priceText = binding.priceInput.text.toString()
        val category = binding.categoryInput.text.toString()
        val condition = binding.conditionInput.text.toString()
        val brand = binding.brandInput.text.toString()
        val color = binding.colorInput.text.toString()

        if(title.isBlank() || description.isBlank() || priceText.isBlank() || category.isBlank() || condition.isBlank()
            || brand.isBlank() || color.isBlank()){
            showToast(fillInAllFields)
            return
        }
        val price = try {
            priceText.toFloat()
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), inputCorrectPrice, Toast.LENGTH_SHORT).show()
            return
        }

        val newItem = Item(
            id = 4,
            sellerId = 1,
            sellerName = "Edi",
            sellerRating = 3F,
            title = title,
            description = description,
            category = category,
            brand = brand,
            condition = condition,
            color = color,
            price = price,
            timestamp = System.currentTimeMillis()
        )
        saveItem(newItem)
    }


    private fun saveItem(item: Item) {
        showToast(itemSuccessfullyAdded)
        clearInputFields()
    }

    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        binding.categoryInput.setAdapter(adapter)
    }

    private fun setupConditionDropdown() {
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, conditions)
        binding.conditionInput.setAdapter(adapter)
    }

    private fun clearInputFields() {
        with(binding) {
            titleInput.text?.clear()
            descriptionInput.text?.clear()
            categoryInput.text?.clear()
            conditionInput.text?.clear()
            brandInput.text?.clear()
            colorInput.text?.clear()
            priceInput.text?.clear()

            titleInput.requestFocus()
        }
    }

}

