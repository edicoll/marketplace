package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.ReviewAdapter
import com.example.projectmarketplace.data.Review
import com.example.projectmarketplace.data.User
import com.example.projectmarketplace.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {

    private lateinit var currentUser: User
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private var reviews: List<Review> = emptyList()
    private lateinit var adapter: ReviewAdapter
    private lateinit var recyclerView: RecyclerView
    private val userKey = "USER_KEY"
    private val reviewKey = "REVIEW_KEY"


    //kreira view
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)

        // dohvaćanje podataka
        currentUser = arguments?.getParcelable(userKey, User::class.java) ?: User(2, "", ",", 3F, "")
        reviews = arguments?.getParcelableArrayList<Review>(reviewKey, Review::class.java) ?: emptyList()

        binding.name.text = currentUser.name

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    //konfiguracija kreiranog viewa
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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