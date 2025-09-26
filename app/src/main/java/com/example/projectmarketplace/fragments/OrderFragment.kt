package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.databinding.FragmentMyordersBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.OrderRepository
import com.example.projectmarketplace.viewModels.OrderViewModel
import com.example.projectmarketplace.views.OrderView
import kotlinx.coroutines.launch

class OrderFragment : BaseFragment<FragmentMyordersBinding>() {


    private var orders: List<Order> = emptyList()
    private var rating: Boolean = false
    private var sellerId: String = ""
    private lateinit var item: Item
    private lateinit var viewModel: OrderViewModel
    private lateinit var orderView: OrderView
    private val ratingKey = "has_rating_key"
    private val sellerIdKey = "sellerId_Key"


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyordersBinding {
        return FragmentMyordersBinding.inflate(inflater, container, false)
    }


    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            orders = bundle.getParcelableArrayList<Order>(orderKey) ?: emptyList()
            rating = bundle.getBoolean(ratingKey)
            sellerId = bundle.getString(sellerIdKey).toString()
            item = bundle.getParcelable<Item>(itemKey)!!
        }


        viewModelInit()

        orderView = OrderView(binding, requireContext(), requireActivity(),
            viewModel, orders, lifecycleOwner = viewLifecycleOwner, sellerId)

        setupBackButton(binding.back)

        orderView.setupRecyclerView()

        lifecycleScope.launch {
            orderView.fetchOrders()
        }

        if(rating)orderView.setSellerReview(item)

    }

    companion object {
        fun newInstance(orders: List<Order>, rating: Boolean, sellerId: String, item: Item): OrderFragment {
            return OrderFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(orderKey, ArrayList(orders))
                    putBoolean(ratingKey, rating)
                    putString(sellerIdKey, sellerId)
                    putParcelable(itemKey, item)
                }
            }
        }
    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OrderViewModel(OrderRepository()) as T
            }
        }).get(OrderViewModel::class.java)
    }

}