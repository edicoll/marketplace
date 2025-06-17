package com.example.projectmarketplace.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Review


class ReviewAdapter (private val reviews: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {


    //čuva podatke za svaki red liste
    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val text: TextView = itemView.findViewById(R.id.text)
        val rating: RatingBar = itemView.findViewById(R.id.ratingBar)


    }

    //kreira se novi UI element, to jest review
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    //postavlja se nakon što se kreira item
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        holder.name.text = review.userNameFrom.toString()
        holder.text.text = review.comment
        holder.rating.rating = review.rating.toFloat()




    }

    override fun getItemCount() = reviews.size
}
