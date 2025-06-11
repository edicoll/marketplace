package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.ReviewAdapter
import com.example.projectmarketplace.data.Review
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentReviewBinding
import com.example.projectmarketplace.fragments.base.BaseFragment


class ReviewFragment : BaseFragment<FragmentReviewBinding>() {

    private var reviews: List<Review> = emptyList()
    private lateinit var adapter: ReviewAdapter
    private lateinit var recyclerView: RecyclerView


    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentReviewBinding {
        return FragmentReviewBinding.inflate(inflater, container, false)
    }


    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // dohvaćanje podataka
        currentUser = arguments?.getParcelable(userKey, User::class.java) ?: User("", "", "", 3.0f)
        reviews = arguments?.getParcelableArrayList<Review>(reviewKey, Review::class.java) ?: emptyList()

        binding.name.text = currentUser?.name

        setupBackButton(binding.back)


        //definira se recycleview i spaja s layoutom
        recyclerView = view.findViewById(R.id.reviewRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = ReviewAdapter(
            reviews
        )
        //rec se spaja s adapterom
        recyclerView.adapter = adapter

    }


}