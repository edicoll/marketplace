package com.example.projectmarketplace.views

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.ItemAdapter
import com.example.projectmarketplace.databinding.FragmentHomeBinding
import com.example.projectmarketplace.viewModels.HomeViewModel

class HomeView(private val binding: FragmentHomeBinding,
               private val context: Context,
               private val activity: FragmentActivity,
               private  var viewModel: HomeViewModel ) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter


    fun setupRecyclerView() {

        recyclerView = binding.itemRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)


        adapter = ItemAdapter(viewModel.items, activity)
        recyclerView.adapter = adapter
    }

    suspend fun fetchItems() {
        viewModel.getItemsExcludingCurrentUser()

        adapter = ItemAdapter(viewModel.items, activity)
        recyclerView.adapter = adapter
    }
}