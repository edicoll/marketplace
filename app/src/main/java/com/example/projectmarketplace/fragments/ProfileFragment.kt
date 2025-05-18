package com.example.projectmarketplace.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.User

class ProfileFragment : Fragment() {

    private lateinit var currentUser: User
    private lateinit var ratingBar: RatingBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Dohvati korisnika
        currentUser = arguments?.getParcelable("USER_KEY") ?: User(2, "ffh", "ww0,", 3F,"hh")

        // Pronađi view-ove i postavi vrijednosti
        val name = view.findViewById<TextView>(R.id.name)
        val email = view.findViewById<TextView>(R.id.email)
        ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)

        name.text = currentUser.name
        email.text = currentUser.email
        ratingBar.rating = currentUser.rating






        return view
    }
}