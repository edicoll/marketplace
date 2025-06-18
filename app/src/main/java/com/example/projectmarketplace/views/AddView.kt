package com.example.projectmarketplace.views

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.projectmarketplace.R
import com.example.projectmarketplace.databinding.FragmentAddBinding
import com.example.projectmarketplace.viewModels.AddViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AddView(private val binding: FragmentAddBinding, private val context: Context,
              private val lifecycleOwner: LifecycleOwner, private  var viewModel: AddViewModel) {

    private val auth = FirebaseAuth.getInstance()

    //toast poruke
    private val fillInAllFields = "Please fill in all fields."
    private val inputCorrectPrice = "Input correct price."
    private val itemSuccessfullyAdded = "Item successfully added!"
    private val itemAddFailed = "Failed to add item. Please try again."
    private val notLoggedIn = "You need to be logged in to add items."

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

        val currentUser = auth.currentUser
        if (currentUser == null) {
            showToast(notLoggedIn)
            return
        }

        if(title.isBlank() || description.isBlank() || priceText.isBlank() || category.isBlank() || condition.isBlank() || brand.isBlank() || color.isBlank()){

            showToast(fillInAllFields)
            return

        }

        val price = try {
            priceText.toFloat()
        } catch (e: NumberFormatException) {
            showToast(inputCorrectPrice)
            return
        }

        lifecycleOwner.lifecycleScope.launch {
            if(viewModel.addItem(title, description, price, category, condition,
                brand, color)){
                showToast(itemSuccessfullyAdded)
                clearFields()
            }else{
                showToast(itemAddFailed)
            }
        }

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
