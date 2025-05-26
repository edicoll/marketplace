package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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

        val filteredItems = if (query.length >= 3) {
            items.filter { item ->
                item.title.contains(query, ignoreCase = true) ||
                        item.description.contains(query, ignoreCase = true) == true  //itemi se filtriraju po naslovu ili po opisu
            }
        }else{
            emptyList()
        }


        adapter.updateItems(filteredItems) // adapter se ažurira s odabranim rezultatima
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

