package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.FavItem
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.data.Review
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentProfileBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.views.ProfileView


val orders = listOf(
        Order(
            id = 1,
            buyerId = 1,
            buyerName = "Edi",
            item = Item(
                id = 1001,
                sellerId = 201,
                sellerName = "Marko Marković",
                sellerRating = 4.5f,
                title = "Samsung Galaxy S23",
                description = "Novi, neotpakiran, garancija 2 godine",
                category = "Mobiteli",
                brand = "Samsung",
                condition = "Novo",
                color = "Crni",
                price = 899.99f,
                timestamp = System.currentTimeMillis()
            ),
            orderDate = System.currentTimeMillis() - 86400000 // jučer
        ),
        Order(
            id = 2,
            buyerId = 1,
            buyerName = "Edi",
            item = Item(
                id = 1002,
                sellerId = 202,
                sellerName = "Petra Petrić",
                sellerRating = 4.8f,
                title = "Apple Watch Series 8",
                description = "Kupljen prošli mjesec, savršeno stanje",
                category = "Pametni satovi",
                brand = "Apple",
                condition = "Kao novo",
                color = "Srebrni",
                price = 499.99f,
                timestamp = System.currentTimeMillis()
            ),
            orderDate = System.currentTimeMillis() - 3600000 // prije sat vremena
        ),
        Order(
            id = 3,
            buyerId = 1,
            buyerName = "Edi",
            item = Item(
                id = 1003,
                sellerId = 203,
                sellerName = "Ivana Ivić",
                sellerRating = 4.2f,
                title = "Sony WH-1000XM5",
                description = "Najbolji bezžični slušalice na tržištu",
                category = "Slušalice",
                brand = "Sony",
                condition = "Novo",
                color = "Plavi",
                price = 399.99f,
                timestamp = System.currentTimeMillis()
            ),
            orderDate = System.currentTimeMillis() - 259200000 // prije 3 dana
        )
    )


val favitems = listOf(
        FavItem(
            id = 1,
            item = Item(
                id = 1001,
                sellerId = 201,
                sellerName = "TechShop",
                sellerRating = 4.7f,
                title = "Samsung Galaxy S23",
                description = "Novi smartphone s najboljim performansama",
                category = "Mobiteli",
                brand = "Samsung",
                condition = "Novo",
                color = "Crni",
                price = 899.99f,
                timestamp = System.currentTimeMillis()
            )
        ),
        FavItem(
            id = 2,
            item = Item(
                id = 1002,
                sellerId = 202,
                sellerName = "AudioExpert",
                sellerRating = 4.9f,
                title = "Sony WH-1000XM5",
                description = "Bežične slušalice s NC tehnologijom",
                category = "Slušalice",
                brand = "Sony",
                condition = "Novo",
                color = "Srebrni",
                price = 399.99f,
                timestamp = System.currentTimeMillis()
            )
        ),
        FavItem(
            id = 3,
            item = Item(
                id = 1003,
                sellerId = 203,
                sellerName = "SportVision",
                sellerRating = 4.5f,
                title = "Nike Air Max 270",
                description = "Udobne sportske tenisice za svaki dan",
                category = "Obuća",
                brand = "Nike",
                condition = "Kao novo",
                color = "Crvene",
                price = 129.99f,
                timestamp = System.currentTimeMillis()
            )
        )
    )


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private var reviews: List<Review> = emptyList()
    private lateinit var profileView: ProfileView

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // dohvaćanje podataka
        currentUser = arguments?.getParcelable(userKey, User::class.java) ?: User(2, "ffh", "ww0,", 3F, "hh")
        reviews = arguments?.getParcelableArrayList<Review>(reviewKey, Review::class.java) ?: emptyList()

        profileView = ProfileView(binding, requireContext(), currentUser)

        profileView.setData()

        setupMyReviews(binding.userInfoContainer)
        setupMyOrders(binding.myOrdersContainer)
        setupFavItems(binding.favItemsContainer)

    }

    private fun setupMyReviews(view: View) {
        view.setOnClickListener {
            val reviewFragment = ReviewFragment()

            val bundle = Bundle().apply {
                putParcelable(userKey, currentUser)
                putParcelableArrayList(reviewKey, ArrayList(reviews))
            }
            reviewFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, reviewFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupMyOrders(view: View) {
        view.setOnClickListener {
            val orderFragment = OrderFragment()

            val bundle = Bundle().apply {
                putParcelable(userKey, currentUser)
                putParcelableArrayList(orderKey, ArrayList(orders))
            }
            orderFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, orderFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupFavItems(view: View) {
        view.setOnClickListener {
            val favItemFragment = FavItemFragment()

            val bundle = Bundle().apply {
                putParcelable(userKey, currentUser)
                putParcelableArrayList(favItemKey, ArrayList(favitems))
            }
            favItemFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, favItemFragment)
                .addToBackStack(null)
                .commit()
        }
    }



}