package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectmarketplace.adapters.SearchAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentSearchBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.views.SearchView


class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    //view binding je mehanizam koji generira klasu koja omogućuje pristup elementima iz XML layouta
    // generira klasu iz layouta npr.FragmentSearchBinding iz fragment_search.xml, ta klsa sadrži refernce na vieowe

    private lateinit var adapter: SearchAdapter
    private var items: List<Item> = emptyList()
    private val electronics = "Electronics"
    private val accessories = "Accessories"
    private val vehicles = "Vehicles"
    private lateinit var searchView: SearchView

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items = arguments?.getParcelableArrayList<Item>(itemKey, Item::class.java) ?: emptyList()

        binding.searchRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = SearchAdapter(
            requireActivity(),
            emptyList()
        )

        searchView = SearchView(binding, requireActivity(), items, adapter)

        binding.searchRecyclerView.adapter = adapter  //na recycleView se postavlja kreirano u adapteru

        searchView.setupSearchBar()

        searchView.setupDropdown()

        searchView.setupCategoryClickListener(binding.electronics, electronics)
        searchView.setupCategoryClickListener(binding.accessories, accessories)
        searchView.setupCategoryClickListener(binding.vehicles, vehicles)

    }





}

