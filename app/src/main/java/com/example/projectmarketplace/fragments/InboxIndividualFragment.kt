package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.MessageAdapter
import com.example.projectmarketplace.data.Message
import com.example.projectmarketplace.databinding.FragmentInboxIndividualBinding
import com.example.projectmarketplace.fragments.base.BaseFragment

//hardkodirane poruke samo za prikaz
val messages = listOf(
    Message(
        id = 1,
        conversationId = 1,
        senderId = 2,
        senderName = "Josip",
        receiverId = 1,
        text = "Hej, kako ide s aplikacijom?",
        timestamp = System.currentTimeMillis() - 3600000
    ),
    Message(
        id = 2,
        conversationId = 2,
        senderId = 2,
        senderName = "Branko",
        receiverId = 1,
        text = "Jesi li vidio moju poruku?",
        timestamp = System.currentTimeMillis() - 86400000,
        isRead = true
    ),Message(
        id = 3,
        conversationId = 1,
        senderId = 2,
        senderName = "Josip",
        receiverId = 1,
        text = "Radi li ova aplikacija?",
        timestamp = System.currentTimeMillis() - 86400000
    ),Message(
        id = 4,
        conversationId = 1,
        senderId = 1,
        senderName = "Edi",
        receiverId = 2,
        text = "Ja šaljem",
        timestamp = System.currentTimeMillis() - 86400000
    )
)

class InboxIndividualFragment : BaseFragment<FragmentInboxIndividualBinding>() {


    private var conversationId: Int? = 0
    private var participant1Id: Int? = 0
    private var participant2Name: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInboxIndividualBinding {
        return FragmentInboxIndividualBinding.inflate(inflater, container, false)
    }

    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //dohvaćanje proslijeđenih podataka
        arguments?.let {
            conversationId = it.getInt("conversationId")
            participant2Name = it.getString("participant2Name")
            participant1Id = it.getInt("participant1Id")
        }

        //back tipka
        setupBackButton(binding.back)

        binding.name.text = participant2Name

        val filteredMessages = messages
            .filter {conversationId == it.conversationId}
            .sortedBy { it.timestamp }


        recyclerView = binding.messagesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = MessageAdapter(
            filteredMessages,
            participant1Id
        )
        recyclerView.adapter = adapter

    }

    //kreacija
    companion object {
        fun newInstance(conversationId: Int, participant2Name: String, participant1Id: Int): InboxIndividualFragment {
            return InboxIndividualFragment().apply {
                arguments = Bundle().apply {
                    putInt("conversationId", conversationId)
                    putString("participant2Name", participant2Name)
                    putInt("participant1Id", participant1Id)
                }
            }
        }
    }
}