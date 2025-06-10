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
    private val itemsFetchFailed = "Failed to fetch items"


    fun setupRecyclerView() {

        recyclerView = binding.itemRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)


        binding.itemRecyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = ItemAdapter(emptyList(), activity)
        binding.itemRecyclerView.adapter = adapter
    }

    fun loadItems() {
        viewModel.getItemsExcludingCurrentUser(
            onSuccess = { items ->

                adapter = ItemAdapter(items, activity)
                recyclerView.adapter = adapter
            },
            onError = { e ->
                Toast.makeText(context, "$itemsFetchFailed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}