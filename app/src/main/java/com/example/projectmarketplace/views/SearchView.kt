package com.example.projectmarketplace.views

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentActivity
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.SearchAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentSearchBinding
import com.example.projectmarketplace.fragments.CategoriesFragment
import kotlin.text.equals


class SearchView(private val binding: FragmentSearchBinding,
                 private val activity: FragmentActivity,
                 private var items: List<Item> = emptyList(),
                 private val adapter: SearchAdapter
                ) {

    private var selectedFilter: String = "Default"
    private var currentDisplayedItems: List<Item> = emptyList()

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
                val sortedItems = applyFilter(currentDisplayedItems)
                adapter.updateItems(sortedItems)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // funkcijaza postavljanje search bara i viewa
    fun setupSearchBar(){
        // postavljanje SearchBara
        binding.searchBar.apply {
            setOnClickListener {
                binding.searchView.show()  //prikazuje se searchView kada se klikne na searchBar
                //binding.searchView.editText.requestFocus()
                binding.searchView.requestFocusAndShowKeyboard()  //prikazivanje tastature
            }
        }

        // postavljanje SearchView-a
        binding.searchView.apply {

            editText.setOnEditorActionListener { _, actionId, _ ->  //postavljanje listenera za akciju pretrage na tastaturi
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(text.toString())     //pretraga kada se pritisne search
                    hide()                 //sakrivanje searchViewa nakon pretrage
                    true
                } else {
                    false
                }
            }
            //postavljanje listenera za live search
            editText.doOnTextChanged { text, _, _, _ ->
                text?.let { performSearch(it.toString()) } //pretraga se izvršava pri svakoj promjeni teksta
                binding.searchBar.setText(text) //prikaz slova u search baru
            }
        }
    }

    // funkcija za pretraživanje
    fun performSearch(query: String){
        val spinner = binding.filtering
        val categories = binding.categories
        val noResult = binding.noResult

        val filteredItems = if (query.length >= 3) {
            val filteredList = items.filter { item ->
                item.title.contains(query, ignoreCase = true) ||
                        item.description.contains(query, ignoreCase = true)
            }

            // upravljanje vizibilnošću elemenata
            spinner.visibility = if (filteredList.isNotEmpty()) View.VISIBLE else View.GONE
            categories.visibility = View.GONE
            noResult.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE

            applyFilter(filteredList)
            filteredList // vraćamo filtriranu listu
        } else {
            spinner.visibility = View.GONE
            categories.visibility = View.VISIBLE
            noResult.visibility = View.GONE
            binding.filtering.setSelection(0)
            emptyList<Item>() // prazna lista se varća
        }

        currentDisplayedItems = filteredItems
        adapter.updateItems(filteredItems) // adapter se ažurira s odabranim rezultatima
    }

    // funkcija kada se filtrira
    private fun applyFilter(list: List<Item>): List<Item> {
        return when (selectedFilter) {
            "Default" -> list.sortedByDescending { it.createdAt }
            "Newest first" -> list.sortedByDescending { it.createdAt }
            "Price: low to high" -> list.sortedBy { it.price }
            "Price: high to low" -> list.sortedByDescending { it.price }
            else -> list
        }
    }

    // funkcija za upravljanje kategorijama
    fun setupCategoryClickListener(view: View, categoryName: String){
        var filteredItems = items.filter { item ->
            item.category.equals(categoryName, ignoreCase = true)
        }

        view.setOnClickListener {  //listener kada se klikne na pojedinu kategoriju

            val fragment = CategoriesFragment.newInstance(categoryName, filteredItems)
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
