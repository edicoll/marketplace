package com.example.projectmarketplace.views

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentHomeIndividualBinding
import com.example.projectmarketplace.fragments.InboxIndividualFragment
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.FragmentActivity
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.fragments.OrderFragment
import com.example.projectmarketplace.viewModels.ItemViewModel


class ItemView(private val binding: FragmentHomeIndividualBinding,
               private val context: Context, private val item: Item,
               private val itemRepository: ItemRepository,
               private val lifecycleOwner: LifecycleOwner,
               private val activity: FragmentActivity,
               private  var viewModel: ItemViewModel
    ) {
    private val auth = FirebaseAuth.getInstance()
    private var boughtItems: List<Order> = emptyList()

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

    suspend fun contactSeller(){
        val conversationId = viewModel.createOrGetConversation(
            currentUserId = auth.currentUser?.uid ?: "",
            sellerId = item.sellerId
        )
        val fragment = InboxIndividualFragment.newInstance(
            conversationId = conversationId,
            participant1Id = auth.currentUser?.uid ?: "",
            participant2Name = itemRepository.getSellerName(item.sellerId).toString()
        )
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()

    }

    suspend fun buyItem(){

        boughtItems = viewModel.buyItem(item)

        val fragment = OrderFragment.newInstance(
            orders = boughtItems
        )
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun setFavItem(){

        lifecycleOwner.lifecycleScope.launch {
            val isFavorite = viewModel.isItemFavorite(item.id)
            updateHeartIcons(isFavorite)
        }


        binding.heartIconBorder.setOnClickListener {
            binding.heartIconBorder.visibility = View.GONE
            binding.heartIconFilled.visibility = View.VISIBLE

            lifecycleOwner.lifecycleScope.launch {
                viewModel.setFavItem(item)
            }
        }
        binding.heartIconFilled.setOnClickListener {
            binding.heartIconFilled.visibility = View.GONE
            binding.heartIconBorder.visibility = View.VISIBLE

            lifecycleOwner.lifecycleScope.launch {
                viewModel.removeFavItem(item)
            }
        }
    }
    private fun updateHeartIcons(isFavorite: Boolean) {
        binding.heartIconBorder.visibility = if (isFavorite) View.GONE else View.VISIBLE
        binding.heartIconFilled.visibility = if (isFavorite) View.VISIBLE else View.GONE
    }

}

