package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectmarketplace.R
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmarketplace.adapters.ConversationAdapter
import com.example.projectmarketplace.data.Conversation
import com.example.projectmarketplace.databinding.FragmentInboxBinding
import com.example.projectmarketplace.fragments.base.BaseFragment


class InboxFragment : BaseFragment<FragmentInboxBinding>() {

    private var conversations: List<Conversation> = emptyList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConversationAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInboxBinding {
        return FragmentInboxBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }
}
