package com.example.projectmarketplace.views

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.ItemAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentHomeBinding
import com.example.projectmarketplace.viewModels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeView(private val binding: FragmentHomeBinding,
               private val context: Context,
               private val activity: FragmentActivity,
               private  var viewModel: HomeViewModel ) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recommendedAdapter: ItemAdapter
    private lateinit var firstItemsAdapter: ItemAdapter
    private lateinit var recentlyViewedAdapter: ItemAdapter
    private lateinit var allItemsAdapter: ItemAdapter
    private var recommendedItems = listOf<Item>()
    private var allItems = listOf<Item>()
    private var first8Items = listOf<Item>()
    private var recentlyViewed = listOf<Item>()
    private var remainingItems = listOf<Item>()


    fun setupRecyclerView() {

        // 1. horizontalni za preporučene
        binding.recommendedRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recommendedAdapter = ItemAdapter(recommendedItems, activity)
        binding.recommendedRecyclerView.adapter = recommendedAdapter

        // 2. prvih 8 od svih
        binding.firstItemsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        firstItemsAdapter = ItemAdapter(first8Items, activity)
        binding.firstItemsRecyclerView.adapter = firstItemsAdapter

        // 3. horizontalni za zadnje pregledane
        binding.recentlyViewedRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recentlyViewedAdapter = ItemAdapter(recentlyViewed, activity)
        binding.recentlyViewedRecyclerView.adapter = recentlyViewedAdapter

        // 4. normalni za sve
        binding.allItemsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        allItemsAdapter = ItemAdapter(remainingItems, activity)
        binding.allItemsRecyclerView.adapter = allItemsAdapter
    }

    suspend fun fetchItems() {

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val allItems = viewModel.getItemsExcludingCurrentUser()

        //postavljaju se preporučeni artikli
        currentUserId?.let {
            recommendedItems = viewModel.getRecommendedItems(it)
            recommendedAdapter.updateItems(recommendedItems)
        }
        if (recommendedItems.isEmpty()) {
            binding.recommendedTitle.visibility = View.GONE
            binding.recommendedRecyclerView.visibility = View.GONE
        } else {
            binding.recommendedTitle.visibility = View.VISIBLE
            binding.recommendedRecyclerView.visibility = View.VISIBLE
        }

        // postavlja se prvih 8 artikala
        first8Items = allItems.take(8)
        firstItemsAdapter.updateItems(first8Items)

        //postavljaju se zadnje pregledani artikli
        recentlyViewed = viewModel.getRecentlyViewedItems()
            // ako nema preporuka gone
        recentlyViewedAdapter.updateItems(recentlyViewed)
        if (recentlyViewed.isEmpty()) {
            binding.recentlyViewedTitle.visibility = View.GONE
            binding.recentlyViewedRecyclerView.visibility = View.GONE
        } else {
            binding.recentlyViewedTitle.visibility = View.VISIBLE
            binding.recentlyViewedRecyclerView.visibility = View.VISIBLE
        }

        // postavljaju se ostali artikli
        remainingItems = allItems.drop(8)
        allItemsAdapter.updateItems(remainingItems)
        if (allItems.size > 8){
            binding.allItemsTitle.visibility = View.VISIBLE
            binding.allItemsRecyclerView.visibility = View.VISIBLE
        }else{
            binding.allItemsTitle.visibility = View.GONE
            binding.allItemsRecyclerView.visibility = View.GONE
        }

    }
}
