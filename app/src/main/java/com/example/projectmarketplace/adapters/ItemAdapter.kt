package com.example.projectmarketplace.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.fragments.ItemFragment
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ItemAdapter (private val items: List<Item>,
                   private val fragmentActivity: FragmentActivity
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    //čuva podatke za svaki red liste
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val price: TextView = itemView.findViewById(R.id.price)
        val date: TextView = itemView.findViewById(R.id.date)

    }

    //kreira se novi UI element, to jest item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_item, parent, false)
        return ViewHolder(view)
    }

    //postavlja se nakon što se kreira item
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.title
        holder.price.text = item.price.toString() + " $"
        holder.date.text = Instant.ofEpochMilli(item.timestamp)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))


        holder.itemView.setOnClickListener {

            val fragment = ItemFragment.newInstance(item)

            fragmentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }

    }

    override fun getItemCount() = items.size
}
