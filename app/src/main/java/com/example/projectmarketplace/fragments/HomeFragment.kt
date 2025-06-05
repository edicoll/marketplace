package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.ItemAdapter
import com.example.projectmarketplace.data.Item

class HomeFragment : Fragment() {

    private var items: List<Item> = emptyList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private val itemKey = "ITEM_KEY"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        items = arguments?.getParcelableArrayList<Item>(itemKey, Item::class.java) ?: emptyList()

        recyclerView = view.findViewById(R.id.itemRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        adapter = ItemAdapter(
            items,
            requireActivity()
        )

        recyclerView.adapter = adapter

        return view
    }
}



