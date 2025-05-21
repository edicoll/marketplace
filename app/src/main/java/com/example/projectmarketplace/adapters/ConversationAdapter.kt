package com.example.projectmarketplace.adapters

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Conversation
import com.example.projectmarketplace.fragments.InboxIndividualFragment
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ConversationAdapter(private val conversations: List<Conversation>,
                          private val fragmentActivity: FragmentActivity
) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {

    //čuva podatke za svaki red liste
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val lastMessage: TextView = itemView.findViewById(R.id.text)
        val time: TextView = itemView.findViewById(R.id.time)

    }

    //kreira se novi UI element, to jest razgovor
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inbox, parent, false)
        return ViewHolder(view)
    }

    //postavlja se nakon što se kreira razgovor
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversation = conversations[position]

        holder.name.text = conversation.participant2Name
        holder.lastMessage.text = conversation.lastMessage
        holder.time.text = Instant.ofEpochMilli(conversation.timestamp)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm"))

        val textColor = if (conversation.unreadCount > 0) Color.GRAY else Color.GRAY
        holder.name.setTextColor(textColor)
        holder.lastMessage.setTextColor(textColor)
        holder.time.setTextColor(textColor)

        holder.itemView.setOnClickListener {

            val fragment = InboxIndividualFragment.newInstance(conversation.id, conversation.participant2Name, conversation.participant1Id)

            fragmentActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }

    }

    override fun getItemCount() = conversations.size
}



