package com.example.projectmarketplace.views

import android.content.Context
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentProfileBinding


class ProfileView(private val binding: FragmentProfileBinding,
                  private val context: Context, private val currentUser: User?
) {

    fun setData(){
        binding.name.text = currentUser?.name
        binding.email.text = currentUser?.email
        binding.ratingBar.rating = currentUser?.rating!!

    }



}