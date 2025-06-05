package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.FavitemAdapter
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.FavItem
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentFavitemBinding
import com.example.projectmarketplace.fragments.base.BaseFragment

class FavItemFragment : BaseFragment<FragmentFavitemBinding>() {

    private var favitems: List<FavItem> = emptyList()
    private lateinit var adapter: FavitemAdapter
    private lateinit var recyclerView: RecyclerView


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavitemBinding {
        return FragmentFavitemBinding.inflate(inflater, container, false)
    }


    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // dohvaćanje podataka
        currentUser = parseUserFromArguments() ?: User(2, "", "", 3F, "")
        favitems = arguments?.getParcelableArrayList<FavItem>(favItemKey, FavItem::class.java) ?: emptyList()

        setupBackButton(binding.back)

        //definira se recycleview i spaja s layoutom
        recyclerView = view.findViewById(R.id.favitemsRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        adapter = FavitemAdapter(favitems)

        //rec se spaja s adapterom
        recyclerView.adapter = adapter

    }

}