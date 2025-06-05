package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Item

class ItemFragment : Fragment() {

    private lateinit var item: Item
    private val itemKey = "ITEM_KEY"
    private val itemNotFound = "Item not found"


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
        item = arguments?.getParcelable(itemKey, Item::class.java) ?: throw IllegalStateException(itemNotFound)


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
        view.findViewById<TextView>(R.id.priceValue).text = view.context.getString(R.string.price_format, item.price)

        view.findViewById<TextView>(R.id.brandInput).text = item.brand
        view.findViewById<TextView>(R.id.descriptionInput).text = item.description
        view.findViewById<TextView>(R.id.conditionInput).text = item.condition
        view.findViewById<TextView>(R.id.colorInput).text = item.color


    }

    companion object {
        fun newInstance(item: Item): ItemFragment {
            return ItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(itemKey, item)
                }
            }
        }
    }
}