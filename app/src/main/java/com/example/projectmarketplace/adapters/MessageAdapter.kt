package com.example.projectmarketplace.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Message
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class MessageAdapter(private var messages: List<Message>, private val participant1Id: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val timeFormat = "HH:mm"


        //2 tipa poslani i primljeni
    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    // viewHolder za poslane poruke
    class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.text)
        val timeText: TextView = itemView.findViewById(R.id.date)
    }

    // viewHolder za primljene poruke
    class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.text)
        val timeText: TextView = itemView.findViewById(R.id.date)
    }

    // ako je poslani poziva se item_sent i obrnuto
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_sent, parent, false)
                SentMessageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_received, parent, false)
                ReceivedMessageViewHolder(view)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       //redni broj poruke
        val message = messages[position]
        //formatiranje vrmena
        val dateFormat = SimpleDateFormat(timeFormat, Locale.getDefault())
        val timeString = dateFormat.format(Date(message.timestamp))

        when (holder) {
            is SentMessageViewHolder -> {
                holder.messageText.text = message.text
                holder.timeText.text = timeString
            }
            is ReceivedMessageViewHolder -> {
                holder.messageText.text = message.text
                holder.timeText.text = timeString

            }
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == participant1Id) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()  //obavještava recycle da se treba ažurirat
    }


}