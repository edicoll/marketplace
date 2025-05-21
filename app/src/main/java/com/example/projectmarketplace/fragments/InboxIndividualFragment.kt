package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.MessageAdapter
import com.example.projectmarketplace.data.Message

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

class InboxIndividualFragment : Fragment() {

    private var conversationId: Int? = 0
    private var participant1Id: Int? = 0
    private var participant2Name: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter

    //kreira view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inbox_individual, container, false)

        //dohvaćanje proslijeđenih podataka
        arguments?.let {
            conversationId = it.getInt("conversationId")
            participant2Name = it.getString("participant2Name")
            participant1Id = it.getInt("participant1Id")
        }

        //back tipka
        view.findViewById<ImageButton>(R.id.back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view
    }

    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.name).text = participant2Name

        val filteredMessages = messages.filter {conversationId == it.conversationId}
            .sortedBy { it.timestamp }


        recyclerView = view.findViewById(R.id.messagesRecyclerView)

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
            val fragment = InboxIndividualFragment()
            val args = Bundle()
            args.putInt("conversationId", conversationId)
            args.putString("participant2Name", participant2Name)
            args.putInt("participant1Id", participant1Id)
            fragment.arguments = args
            return fragment
        }
    }
}