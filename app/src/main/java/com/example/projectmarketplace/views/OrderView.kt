package com.example.projectmarketplace.views

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.OrderAdapter
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.databinding.FragmentMyordersBinding
import com.example.projectmarketplace.viewModels.OrderViewModel

class OrderView(private val binding: FragmentMyordersBinding,
                private val context: Context,
                private val activity: FragmentActivity,
                private  var viewModel: OrderViewModel,
                private val orders: List<Order>) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter


    fun setupRecyclerView() {

        recyclerView = binding.myordersRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = OrderAdapter(orders)
        recyclerView.adapter = adapter
    }

    suspend fun fetchOrders(){
        viewModel.fetchOrders()

        adapter = OrderAdapter(viewModel.orders)
        recyclerView.adapter = adapter
    }

}