package com.example.projectmarketplace.adapters

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Message
import com.example.projectmarketplace.fragments.InboxIndividualFragment
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MessageAdapter(private val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val text: TextView = itemView.findViewById(R.id.text)
        val date: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.name.text = message.senderName
        holder.text.text = message.text
        holder.date.text = Instant.ofEpochMilli(message.timestamp)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm"))

        val textColor = if (message.isRead) Color.GRAY else Color.BLACK
        holder.name.setTextColor(textColor)
        holder.text.setTextColor(textColor)
        holder.date.setTextColor(textColor)

        holder.itemView.setOnClickListener {
            message.isRead = true
            notifyItemChanged(position)

            val fragmentIndividual = InboxIndividualFragment.newInstance(message)
            val transaction = (holder.itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, fragmentIndividual)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }



    override fun getItemCount(): Int = messages.size
}