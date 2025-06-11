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
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class ItemAdapter (private val items: List<Item>,
                   private val fragmentActivity: FragmentActivity
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    val dateFormat = "dd.MM.yyyy."


    //čuva podatke za svaki red liste
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val price: TextView = itemView.findViewById(R.id.price)
        val date: TextView = itemView.findViewById(R.id.date)

    }

    //kreira se novi UI element, to jest item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_item, parent, false)
        return ViewHolder(view)
    }

    //postavlja se nakon što se kreira item
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.title
        holder.price.text = holder.itemView.context.getString(R.string.price_format, item.price)
        holder.date.text = SimpleDateFormat(dateFormat, Locale.getDefault()).format(item.createdAt)



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
