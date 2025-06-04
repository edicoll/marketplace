package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.OrderAdapter
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentMyordersBinding

class OrderFragment : Fragment() {

    private lateinit var currentUser: User
    private var _binding: FragmentMyordersBinding? = null
    private val binding get() = _binding!!
    private var orders: List<Order> = emptyList()
    private lateinit var adapter: OrderAdapter
    private lateinit var recyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyordersBinding.inflate(inflater, container, false)

        // dohvaćanje podataka
        currentUser = arguments?.getParcelable("USER_KEY", User::class.java) ?: User(2, "ffh", "ww0,", 3F, "hh")
        orders = arguments?.getParcelableArrayList<Order>("ORDER_KEY", Order::class.java) ?: emptyList()

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    //konfiguracija kreiranog viewa
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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