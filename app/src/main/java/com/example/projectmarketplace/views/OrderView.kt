package com.example.projectmarketplace.views

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.OrderAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.databinding.FragmentMyordersBinding
import com.example.projectmarketplace.viewModels.OrderViewModel
import kotlinx.coroutines.launch

class OrderView(private val binding: FragmentMyordersBinding,
                private val context: Context,
                private val activity: FragmentActivity,
                private  var viewModel: OrderViewModel,
                private val orders: List<Order>,
                private val lifecycleOwner: LifecycleOwner,
                private val sellerId: String) {

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

    fun setSellerReview(item: Item){
        binding.sellerRating.visibility = View.VISIBLE
        binding.blurOverlay.visibility = View.VISIBLE

        lifecycleOwner.lifecycleScope.launch {
            val sellerName = viewModel.getUserName(sellerId)
            binding.ratingtoName.text = sellerName
        }

        binding.btnSubmit.setOnClickListener {

            val rating = binding.ratingBar.rating.toInt()
            val comment = binding.ratingComment.text.toString()

            lifecycleOwner.lifecycleScope.launch {
                viewModel.setReview(sellerId, rating, comment, item)
            }

            binding.sellerRating.visibility = View.GONE
            binding.blurOverlay.visibility = View.GONE
        }
    }

}