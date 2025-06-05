package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.ItemAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentHomeBinding
import com.example.projectmarketplace.fragments.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private var items: List<Item> = emptyList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items = arguments?.getParcelableArrayList<Item>(itemKey, Item::class.java) ?: emptyList()

        recyclerView = view.findViewById(R.id.itemRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        adapter = ItemAdapter(
            items,
            requireActivity()
        )

        recyclerView.adapter = adapter

    }
}



