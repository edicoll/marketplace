package com.example.projectmarketplace.views

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentAddBinding

class AddView(private val binding: FragmentAddBinding, private val context: Context) {

    //dohvaćanje unesenih vrijednosti
    private val fillInAllFields = "Please fill in all fields."
    private val inputCorrectPrice = "Input correct price."
    private val itemSuccessfullyAdded = "Item successfully added!"

    fun setupCategoryDropdown(categories: List<String>) {
        val adapter = ArrayAdapter(context, R.layout.dropdown_item, categories)
        binding.categoryInput.setAdapter(adapter)
    }

    fun setupConditionDropdown(conditions: List<String>) {
        val adapter = ArrayAdapter(context, R.layout.dropdown_item, conditions)
        binding.conditionInput.setAdapter(adapter)
    }

    fun handleSaveButtonClick(){
        //dohvaćanje unesenih vrijednosti
        val title = binding.titleInput.text.toString()
        val description = binding.descriptionInput.text.toString()
        val priceText = binding.priceInput.text.toString()
        val category = binding.categoryInput.text.toString()
        val condition = binding.conditionInput.text.toString()
        val brand = binding.brandInput.text.toString()
        val color = binding.colorInput.text.toString()

        val price = try {
            priceText.toFloat()
        } catch (e: NumberFormatException) {
            showToast(inputCorrectPrice)
            return
        }

        if(title.isBlank() || description.isBlank() || priceText.isBlank() || category.isBlank() || condition.isBlank() || brand.isBlank() || color.isBlank()){

            showToast(fillInAllFields)
            return

        }else{
                //dodavanje
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
        clearFields()
    }

    fun clearFields() {
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

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
