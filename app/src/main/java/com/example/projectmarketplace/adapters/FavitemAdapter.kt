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
import java.util.Locale

class FavitemAdapter (private val favitems: List<Item>,
                      private val fragmentActivity: FragmentActivity
) : RecyclerView.Adapter<FavitemAdapter.FavitemViewHolder>() {

    val dateFormat = "dd.MM.yyyy."

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

        holder.title.text = favitem.title
        holder.price.text = holder.itemView.context.getString(R.string.price_format, favitem.price)
        holder.date.text = SimpleDateFormat(dateFormat, Locale.getDefault()).format(favitem.createdAt)


        holder.itemView.setOnClickListener {

            val fragment = ItemFragment.newInstance(favitem)

            fragmentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount() = favitems.size
}