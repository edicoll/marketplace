package com.example.projectmarketplace.views

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.SearchAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentCategoriesBinding

class CategoriesView(private val binding: FragmentCategoriesBinding,
                 private val activity: FragmentActivity,
                 private var items: List<Item> = emptyList(),
                 private val adapter: SearchAdapter
) {
    private var selectedFilter: String = "Default"

    // funkcija za postavljanje spinnera
    fun setupDropdown(){
        val spinner = binding.filtering // dohvaća referencu na spinner
        ArrayAdapter.createFromResource( // kreira se adapter koji postavlja filtere i dizajn
            activity,
            R.array.filter_options,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item) //layout za prikaz padajućeg izbornika
            spinner.adapter = adapter //postavlja kreirani adapter na spinner
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedFilter = parent.getItemAtPosition(position).toString()
                val sortedItems = applyFilter(items)
                adapter.updateItems(sortedItems)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // funkcija kada se filtrira
    private fun applyFilter(list: List<Item>): List<Item> {
        return when (selectedFilter) {
            "Default" -> list.sortedByDescending { it.timestamp }
            "Newest first" -> list.sortedByDescending { it.timestamp }
            "Price: low to high" -> list.sortedBy { it.price }
            "Price: high to low" -> list.sortedByDescending { it.price }
            else -> list
        }
    }

}
