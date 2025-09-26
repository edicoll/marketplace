package com.example.projectmarketplace.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Order
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class OrderAdapter (private val orders: List<Order>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    val dateFormat = "dd.MM.yyyy."

    //čuva podatke za svaki red liste
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val price: TextView = itemView.findViewById(R.id.price)
        val date: TextView = itemView.findViewById(R.id.date)
        val image: ImageView = itemView.findViewById(R.id.image)

    }

    //kreira se novi UI element, to jest review
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    //postavlja se nakon što se kreira item
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.title.text = order.title
        holder.price.text = holder.itemView.context.getString(R.string.price_format, order.price)

        holder.date.text = SimpleDateFormat(dateFormat, Locale.getDefault()).format(order.orderDate)

        // Učitavanje slike
        order.imageUrl.let { imageUrl ->
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(holder.image)
        }
    }

    override fun getItemCount() = orders.size
}


