package com.example.projectmarketplace.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.FavItem
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class FavitemAdapter (private val favitems: List<FavItem>
) : RecyclerView.Adapter<FavitemAdapter.FavitemViewHolder>() {


    //čuva podatke za svaki red liste
    class FavitemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val price: TextView = itemView.findViewById(R.id.price)
        val date: TextView = itemView.findViewById(R.id.date)

    }

    //kreira se novi UI element, to jest review
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavitemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_item, parent, false)
        return FavitemViewHolder(view)
    }

    //postavlja se nakon što se kreira item
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FavitemViewHolder, position: Int) {
        val favitem = favitems[position]

        holder.title.text = favitem.item.title
        holder.price.text = favitem.item.price.toString() + "$"
        holder.date.text = Instant.ofEpochMilli(favitem.item.timestamp)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))

    }

    override fun getItemCount() = favitems.size
}