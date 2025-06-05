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
import com.example.projectmarketplace.adapters.ConversationAdapter
import com.example.projectmarketplace.data.Conversation


class InboxFragment : Fragment() {

    private var conversations: List<Conversation> = emptyList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConversationAdapter
    private val conversationKey = "CONVERSATION_KEY"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //dohvaća se view
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)

        //dohvaćaju se podatci o razgovorima
        conversations = arguments?.getParcelableArrayList<Conversation>(conversationKey, Conversation::class.java) ?: emptyList()

        //definira se recycleview i spaja s layoutom
        recyclerView = view.findViewById(R.id.inboxRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        //u adapter se šalju razgovori
        adapter = ConversationAdapter(
            conversations,
            requireActivity()
        )
        //rec se spaja s adapterom
        recyclerView.adapter = adapter

        return view
    }
}
