package com.example.projectmarketplace.views

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentHomeIndividualBinding


class ItemView(private val binding: FragmentHomeIndividualBinding,
               private val context: Context, private val item: Item,
               private val itemRepository: ItemRepository,
               private val lifecycleOwner: LifecycleOwner
    ) {

    fun bind(){

        loadSellerInfo()

        with(binding) {
            title.text = item.title
            priceValue.text = context.getString(R.string.price_format, item.price)
            brandInput.text = item.brand
            descriptionInput.text = item.description
            conditionInput.text = item.condition
            colorInput.text = item.color
        }
    }

    private fun loadSellerInfo() {
        lifecycleOwner.lifecycleScope.launch {
            val name = itemRepository.getSellerName(item.sellerId)
            val rating = itemRepository.getSellerRating(item.sellerId)

            binding.sellerName.text = name
            binding.sellerRating.rating = rating!!
        }
    }

}

