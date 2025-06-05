package com.example.projectmarketplace.views

import android.content.Context

import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentHomeIndividualBinding


class ItemView(private val binding: FragmentHomeIndividualBinding,
               private val context: Context, private val item: Item ) {

    fun bind(){
        with(binding) {
            title.text = item.title
            sellerName.text = item.sellerName
            sellerRating.rating = item.sellerRating
            priceValue.text = context.getString(R.string.price_format, item.price)

            brandInput.text = item.brand
            descriptionInput.text = item.description
            conditionInput.text = item.condition
            colorInput.text = item.color
        }
    }



}
