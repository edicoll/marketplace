package com.example.projectmarketplace.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.ItemItemBinding
import com.example.projectmarketplace.fragments.ItemFragment
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class SearchAdapter(
    private val fragmentActivity: FragmentActivity,
    private var items: List<Item>
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    val dateFormat = "dd.MM.yyyy."

    // funkcija koja se poziva iz searchFragmenta za ažuriranje liste item-a
    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: List<Item>) {
        items = newItems   // items se nekako prosljeđuje ovdje
        notifyDataSetChanged() // obavještava adapter
    }

    // funkcija za čišćenje
    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        items = emptyList()   // prazna lista
        notifyDataSetChanged() // obavještava adapter
    }

    //stvara novi viewholder kada je potrebno u recycleviewu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemItemBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    //povezuje podatke s viewholderom
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SearchViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: Item) {
            binding.apply {

                // postavlja podatke na view-ove
                title.text = item.title
                price.text = itemView.context.getString(R.string.price_format, item.price)
                date.text = item.createdAt.toString()
                date.text = SimpleDateFormat(dateFormat, Locale.getDefault()).format(item.createdAt)

                itemView.setOnClickListener { // klikom na pojedini item se prikazuje novi fragment

                    val fragment = ItemFragment.newInstance(item)

                    fragmentActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, fragment)
                        .addToBackStack(null)
                        .commit()
                }


            }
        }
    }
}