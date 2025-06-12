package com.example.projectmarketplace.views

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.ConversationAdapter
import com.example.projectmarketplace.databinding.FragmentInboxBinding
import com.example.projectmarketplace.viewModels.InboxViewModel

class InboxView(private val binding: FragmentInboxBinding,
                private val context: Context,
                private val activity: FragmentActivity,
                private  var viewModel: InboxViewModel) {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConversationAdapter


    fun setupRecyclerView() {

        recyclerView = binding.inboxRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)


        adapter = ConversationAdapter(viewModel.conversations, activity)
        recyclerView.adapter = adapter
    }

    suspend fun fetchConversations() {
        viewModel.getConversations()

        adapter = ConversationAdapter(viewModel.conversations, activity)
        recyclerView.adapter = adapter
    }
}