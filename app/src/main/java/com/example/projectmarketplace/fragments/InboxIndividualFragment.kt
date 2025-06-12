package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
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



class InboxIndividualFragment : BaseFragment<FragmentInboxIndividualBinding>() {


    private var conversationId: String? = null
    private var participant1Id: String? = null
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
            conversationId = it.getString("conversationId")
            participant1Id = it.getString("participant1Id")
            participant2Name = it.getString("participant2Name")
        }

        Log.d("conversationId", " mora raditi svaki put $conversationId, provjera")


        binding.name.text = participant2Name

        // tu nastavljaName
        /*
        //back tipka
        setupBackButton(binding.back)



        val filteredMessages = messages
            .filter {conversationId == it.conversationId}
            .sortedBy { it.timestamp }


        recyclerView = binding.messagesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

       /* adapter = MessageAdapter(
            filteredMessages,
            participant1Id
        )*/
        recyclerView.adapter = adapter*/

    }

    //kreacija
    companion object {
        fun newInstance(conversationId: String, participant1Id: String, participant2Name: String): InboxIndividualFragment {
            return InboxIndividualFragment().apply {
                arguments = Bundle().apply {
                    putString("conversationId", conversationId)
                    putString("participant1Id", participant1Id)
                    putString("participant2Name", participant2Name)
                }
            }
        }
    }
}