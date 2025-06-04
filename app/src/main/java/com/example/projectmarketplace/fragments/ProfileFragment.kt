package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Review
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentProfileBinding

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

        setupActionClickListener(binding.userInfoContainer)

    }

    private fun setupActionClickListener(view: View) {
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}