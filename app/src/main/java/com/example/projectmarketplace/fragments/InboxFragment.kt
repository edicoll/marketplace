package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectmarketplace.R
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.MessageAdapter
import com.example.projectmarketplace.data.Message



class InboxFragment : Fragment() {

    private var messages: List<Message> = emptyList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_inbox, container, false)

        messages = arguments?.getParcelableArrayList<Message>("MESSAGES_KEY", Message::class.java) ?: emptyList()

        recyclerView = view.findViewById(R.id.messagesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = MessageAdapter(messages)
        recyclerView.adapter = adapter

        return view
    }
}
