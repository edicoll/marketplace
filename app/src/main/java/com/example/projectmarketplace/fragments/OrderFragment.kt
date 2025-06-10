package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.OrderAdapter
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentMyordersBinding
import com.example.projectmarketplace.fragments.base.BaseFragment

class OrderFragment : BaseFragment<FragmentMyordersBinding>() {


    private var orders: List<Order> = emptyList()
    private lateinit var adapter: OrderAdapter
    private lateinit var recyclerView: RecyclerView

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

        currentUser = arguments?.getParcelable(userKey, User::class.java) ?: User("", "", ",", 3F)
        orders = arguments?.getParcelableArrayList<Order>(orderKey, Order::class.java) ?: emptyList()

        setupBackButton(binding.back)

        //definira se recycleview i spaja s layoutom
        recyclerView = view.findViewById(R.id.myordersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = OrderAdapter(
            orders
        )
        //rec se spaja s adapterom
        recyclerView.adapter = adapter

    }

}