package com.example.projectmarketplace.views

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.FavitemAdapter
import com.example.projectmarketplace.databinding.FragmentFavitemBinding
import com.example.projectmarketplace.viewModels.FavItemViewModel

class FavItemView (private val binding: FragmentFavitemBinding,
                   private val context: Context,
                   private val activity: FragmentActivity,
                   private  var viewModel: FavItemViewModel ) {

    private lateinit var adapter: FavitemAdapter
    private lateinit var recyclerView: RecyclerView

    fun setupRecyclerView() {

        recyclerView = binding.favitemsRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        adapter = FavitemAdapter(emptyList(), activity)

        recyclerView.adapter = adapter
    }

    suspend fun fetchFavItems() {
        viewModel.fetchFavItems()
        val favItems = viewModel.fetchFavItems()

        adapter = FavitemAdapter(favItems, activity)
        recyclerView.adapter = adapter
    }



}