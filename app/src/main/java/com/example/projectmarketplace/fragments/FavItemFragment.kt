package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.FavitemAdapter
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.FavItem
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentFavitemBinding

class FavItemFragment : Fragment(){

    private lateinit var currentUser: User
    private var _binding: FragmentFavitemBinding? = null
    private val binding get() = _binding!!
    private var favitems: List<FavItem> = emptyList()
    private lateinit var adapter: FavitemAdapter
    private lateinit var recyclerView: RecyclerView
    private val userKey = "USER_KEY"
    private val favItemKey = "FAVITEM_KEY"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavitemBinding.inflate(inflater, container, false)

        // dohvaćanje podataka
        currentUser = arguments?.getParcelable(userKey, User::class.java) ?: User(2, "", ",", 3F, "")
        favitems = arguments?.getParcelableArrayList<FavItem>(favItemKey, FavItem::class.java) ?: emptyList()


        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    //konfiguracija kreiranog viewa
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //definira se recycleview i spaja s layoutom
        recyclerView = view.findViewById(R.id.favitemsRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        adapter = FavitemAdapter(
            favitems
        )
        //rec se spaja s adapterom
        recyclerView.adapter = adapter

    }

}