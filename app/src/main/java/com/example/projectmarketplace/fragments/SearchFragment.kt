package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectmarketplace.adapters.SearchAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentSearchBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.viewModels.HomeViewModel
import com.example.projectmarketplace.viewModels.SearchViewModel
import com.example.projectmarketplace.views.SearchView
import kotlinx.coroutines.launch


class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    //view binding je mehanizam koji generira klasu koja omogućuje pristup elementima iz XML layouta
    // generira klasu iz layouta npr.FragmentSearchBinding iz fragment_search.xml, ta klsa sadrži refernce na vieowe

    private lateinit var adapter: SearchAdapter
    private var items: List<Item> = emptyList()
    private lateinit var searchView: SearchView
    private lateinit var viewModel: SearchViewModel
    val categories = listOf(
        "Electronics",
        "Accessories",
        "Vehicles"
    )

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInit()

        binding.searchRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = SearchAdapter(
            requireActivity(),
            emptyList()
        )

        searchView = SearchView(binding, requireActivity(), adapter, viewModel)

        lifecycleScope.launch {
            searchView.fetchItems()
        }

        binding.searchRecyclerView.adapter = adapter  //na recycleView se postavlja kreirano u adapteru

        searchView.setupSearchBar()

        searchView.setupDropdown()

        searchView.setupCategoryClickListener(categories)

    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(ItemRepository()) as T
            }
        }).get(SearchViewModel::class.java)
    }



}

