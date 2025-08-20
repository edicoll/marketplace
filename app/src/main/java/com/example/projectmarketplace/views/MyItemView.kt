package com.example.projectmarketplace.views


import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.FavitemAdapter
import com.example.projectmarketplace.adapters.MyItemAdapter
import com.example.projectmarketplace.databinding.FragmentMyitemBinding
import com.example.projectmarketplace.viewModels.MyItemViewModel

class MyItemView (private val binding: FragmentMyitemBinding,
                   private val context: Context,
                   private val activity: FragmentActivity,
                   private  var viewModel: MyItemViewModel ) {

    private lateinit var adapter: MyItemAdapter
    private lateinit var recyclerView: RecyclerView

    fun setupRecyclerView() {

        recyclerView = binding.myitemRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        adapter = MyItemAdapter(emptyList(), activity)

        recyclerView.adapter = adapter
    }

    suspend fun fetchMyItems() {

        val myItems = viewModel.fetchMyItems()

        adapter = MyItemAdapter(myItems, activity)
        recyclerView.adapter = adapter
    }



}