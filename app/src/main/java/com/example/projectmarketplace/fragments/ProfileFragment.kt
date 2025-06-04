package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
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


class ProfileFragment : Fragment() {

    private lateinit var currentUser: User
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var reviews: List<Review> = emptyList()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // dohvaćanje podataka
        currentUser = arguments?.getParcelable("USER_KEY", User::class.java) ?: User(2, "ffh", "ww0,", 3F, "hh")
        reviews = arguments?.getParcelableArrayList<Review>("REVIEW_KEY", Review::class.java) ?: emptyList()

        val name = binding.name
        val email = binding.email
        val ratingBar = binding.ratingBar

        name.text = currentUser.name
        email.text = currentUser.email
        ratingBar.rating = currentUser.rating

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMyReviews(binding.userInfoContainer)
        setupMyOrders(binding.myOrdersContainer)
        setupFavItems(binding.favItemsContainer)

    }

    private fun setupMyReviews(view: View) {
        view.setOnClickListener {
            val reviewFragment = ReviewFragment()

            val bundle = Bundle().apply {
                putParcelable("USER_KEY", currentUser)
                putParcelableArrayList("REVIEW_KEY", ArrayList(reviews))
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
                putParcelable("USER_KEY", currentUser)
                putParcelableArrayList("ORDER_KEY", ArrayList(orders))
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
                putParcelable("USER_KEY", currentUser)
                putParcelableArrayList("FAVITEM_KEY", ArrayList(favitems))
            }
            favItemFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, favItemFragment)
                .addToBackStack(null)
                .commit()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}