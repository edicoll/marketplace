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
import com.example.projectmarketplace.data.Review
import com.example.projectmarketplace.databinding.FragmentProfileBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ProfileRepository
import com.example.projectmarketplace.viewModels.ProfileViewModel
import com.example.projectmarketplace.views.ProfileView


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

        profileView = ProfileView(binding, requireContext(),
            viewModel, lifecycleOwner = viewLifecycleOwner, requireActivity())

        profileView.setupUserInfo()
        profileView.setupLogout()
        profileView.deleteAccount()

        setupMyReviews(binding.userInfoContainer)
        setupMyOrders(binding.myOrdersContainer)
        setupFavItems(binding.favItemsContainer)
        setupMyItems(binding.myItemsContainer)

    }

    private fun setupMyReviews(view: View) {
        view.setOnClickListener {
            val reviewFragment = ReviewFragment()

            val bundle = Bundle().apply {
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
                orders = emptyList(),
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
                putParcelableArrayList(favItemKey, ArrayList(emptyList()))
            }
            favItemFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, favItemFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupMyItems(view: View) {
        view.setOnClickListener {
            val myItemFragment = MyItemFragment()

            val bundle = Bundle().apply {
                putParcelable(userKey, currentUser)
                putParcelableArrayList(myItemKey, ArrayList(emptyList()))
            }
            myItemFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, myItemFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(ProfileRepository()) as T
            }
        }).get(ProfileViewModel::class.java)
    }

}