package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.FavItem
import com.example.projectmarketplace.data.Order
import com.example.projectmarketplace.data.Review
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentProfileBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.viewModels.ProfileViewModel
import com.example.projectmarketplace.views.ProfileView


val orders = emptyList<Order>()
    /*listOf(
        Order(
            id = 1,
            buyerId = 1,
            buyerName = "Edi",
            Item(
                title = "kola",
                description = "Brand new car fiat panda.",
                price = 5000.00,
                brand = "Fiat",
                condition = "new",
                sellerId = "2",
                color = "white",
                createdAt = Date(System.currentTimeMillis() - 486400000),
                category = "Vehicles"
            ),
            orderDate = System.currentTimeMillis() - 86400000 // jučer
        ),
        Order(
            id = 2,
            buyerId = 1,
            buyerName = "Edi",
            Item(
                title = "ferrari",
                description = "Brand new car fiat panda.",
                price = 50300.00,
                brand = "Fiat",
                condition = "new",
                sellerId = "2",
                color = "white",
                createdAt = Date(System.currentTimeMillis() - 486400000),
                category = "Vehicles"
            ),
            orderDate = System.currentTimeMillis() - 3600000 // prije sat vremena
        ),
        Order(
            id = 3,
            buyerId = 1,
            buyerName = "Edi",
            Item(
                title = "Auto mercede",
                description = "Brand new car fiat panda.",
                price = 1000.00,
                brand = "Fiat",
                condition = "new",
                sellerId = "2",
                color = "white",
                createdAt = Date(System.currentTimeMillis() - 486400000),
                category = "Vehicles"
            ),
            orderDate = System.currentTimeMillis() - 259200000 // prije 3 dana
        )
    )*/


val favitems = emptyList<FavItem>()
    /*listOf(
        FavItem(
            id = 1,
            item = Item(
                title = "pametni sat",
                description = "Brand new car fiat panda.",
                price = 5000.00,
                brand = "Fiat",
                condition = "new",
                sellerId = "2",
                color = "white",
                createdAt = Date(System.currentTimeMillis() - 486400000),
                category = "Vehicles"
            ),
        ),
        FavItem(
            id = 2,
            Item(
                title = "haloo",
                description = "Brand new car fiat panda.",
                price = 5000.00,
                brand = "Fiat",
                condition = "new",
                sellerId = "2",
                color = "white",
                createdAt = Date(System.currentTimeMillis() - 486400000),
                category = "Vehicles"
            ),
        ),
        FavItem(
            id = 3,
            Item(
                title = "wd40",
                description = "Brand new car fiat panda.",
                price = 5000.00,
                brand = "Fiat",
                condition = "new",
                sellerId = "2",
                color = "white",
                createdAt = Date(System.currentTimeMillis() - 486400000),
                category = "Vehicles"
            ),
        )
    )*/


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private var reviews: List<Review> = emptyList()
    private lateinit var profileView: ProfileView
    private lateinit var viewModel: ProfileViewModel

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInit()
        // dohvaćanje podataka
        currentUser = arguments?.getParcelable(userKey, User::class.java) ?: User("", "ffh", "ww0,", 3.0f, 0)
        reviews = arguments?.getParcelableArrayList<Review>(reviewKey, Review::class.java) ?: emptyList()

        profileView = ProfileView(binding, requireContext(),
            viewModel, lifecycleOwner = viewLifecycleOwner)

        profileView.setupUserInfo()
        profileView.setupLogout()

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
            val fragment = OrderFragment.newInstance(
                orders = orders,
                rating = false,
                sellerId = ""
            )

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.flFragment, fragment)
                ?.addToBackStack(null)
                ?.commit()
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

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(ItemRepository()) as T
            }
        }).get(ProfileViewModel::class.java)
    }

}