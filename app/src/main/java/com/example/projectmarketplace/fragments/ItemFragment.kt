package com.example.projectmarketplace.fragments

import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.MessageAdapter
import com.example.projectmarketplace.data.Item

class ItemFragment : Fragment() {

    private lateinit var item: Item


    //kreira view
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_individual, container, false)

        //dohvaćanje proslijeđenih podataka, getParceable je isto kao pojedinačno
        //dohvaća za cijeli objekt Item
        item = arguments?.getParcelable("item", Item::class.java) ?: throw IllegalStateException("Item not found")


        //back tipka
        view.findViewById<ImageButton>(R.id.back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view
    }

    //konfiguracija kreiranog viewa
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.title).text = item.title
        view.findViewById<TextView>(R.id.sellerName).text = item.sellerName
        view.findViewById<RatingBar>(R.id.sellerRating).rating = item.sellerRating
        view.findViewById<TextView>(R.id.priceValue).text = item.price.toString() + " $"

        view.findViewById<TextView>(R.id.brandInput).text = item.brand
        view.findViewById<TextView>(R.id.descriptionInput).text = item.description
        view.findViewById<TextView>(R.id.conditionInput).text = item.condition
        view.findViewById<TextView>(R.id.colorInput).text = item.color


    }

    companion object {
        fun newInstance(item: Item): ItemFragment {
            return ItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("item", item)
                }
            }
        }
    }
}