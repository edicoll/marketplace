package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.SearchAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    //view binding je mehanizam koji generira klasu koja omogućuje pristup elementima iz XML layouta
    // generira klasu iz layouta npr.FragmentSearchBinding iz fragment_search.xml, ta klsa sadrži refernce na vieowe
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchAdapter
    private var items: List<Item> = emptyList()
    private var selectedFilter: String = "Default"
    private var currentDisplayedItems: List<Item> = emptyList()



    //kreiranje binding objekta
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root //ovo zapravo vraća glavni view, to je u mom slučaju constrainedlayou
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items = arguments?.getParcelableArrayList<Item>("ITEM_KEY", Item::class.java) ?: emptyList()

        binding.searchRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = SearchAdapter(
            requireActivity(),
            emptyList()
        )
        binding.searchRecyclerView.adapter = adapter  //na recycleView se postavlja kreirano u adapteru
        setupRecyclerView()

        val spinner = binding.filtering // dohvaća referencu na spinner
        ArrayAdapter.createFromResource( // kreira se adapter koji postavlja filtere i dizajn
            requireContext(),
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

        setupCategoryClickListener(binding.electronics, "electronics")
        setupCategoryClickListener(binding.accessories, "accessories")
        setupCategoryClickListener(binding.vehicles, "vehicles")

    }


    private fun setupRecyclerView() {

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


    private fun performSearch(query: String) {
        val spinner = binding.filtering
        val categories = binding.categories
        val noResult = binding.noResult


        val filteredItems = if (query.length >= 3) {
            items.filter { item ->
                item.title.contains(query, ignoreCase = true) ||
                        item.description.contains(query, ignoreCase = true)
            }.let { filteredList ->

                spinner.visibility = if (filteredList.isNotEmpty()) View.VISIBLE else View.GONE //sakrivanje i prikazivanje spinnera
                categories.visibility = View.GONE //sakrivanje kategorija
                noResult.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE //sakrivanje i prikazivanje nema rezultata
                applyFilter(filteredList) }
        } else {
            spinner.visibility = View.GONE
            categories.visibility = View.VISIBLE  //prikazivanje kategorija
            noResult.visibility = View.GONE
            emptyList()
        }

        currentDisplayedItems = filteredItems
        adapter.updateItems(filteredItems) // adapter se ažurira s odabranim rezultatima
    }

    private fun applyFilter(list: List<Item>): List<Item> {
        return when (selectedFilter) {
            "Newest first" -> list.sortedByDescending { it.timestamp }
            "Price: low to high" -> list.sortedBy { it.price }
            "Price: high to low" -> list.sortedByDescending { it.price }
            else -> list
        }
    }

    private fun setupCategoryClickListener(view: View, categoryName: String) {
        view.setOnClickListener {
            val filteredItems = items.filter { item ->
                item.category.equals(categoryName, ignoreCase = true)
            }
            binding.categories.visibility = View.GONE
            binding.filtering.visibility = View.VISIBLE
            currentDisplayedItems = filteredItems
            adapter.updateItems(applyFilter(filteredItems))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

