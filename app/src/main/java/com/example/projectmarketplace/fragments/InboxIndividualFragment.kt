package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.projectmarketplace.databinding.FragmentInboxIndividualBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.MessageRepository
import com.example.projectmarketplace.viewModels.InboxIndividualViewModel
import com.example.projectmarketplace.views.InboxIndividualView
import kotlinx.coroutines.launch


class InboxIndividualFragment : BaseFragment<FragmentInboxIndividualBinding>() {


    private var conversationId: String? = null
    private var participant1Id: String? = null
    private var participant2Name: String? = null
    private lateinit var viewModel: InboxIndividualViewModel
    private lateinit var inboxIndividualView: InboxIndividualView
    private val repository = MessageRepository()

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

        viewModelInit()

        //dohvaćanje proslijeđenih podataka
        arguments?.let {
            conversationId = it.getString("conversationId")
            participant1Id = it.getString("participant1Id")
            participant2Name = it.getString("participant2Name")
        }
        binding.name.text = participant2Name

        inboxIndividualView = InboxIndividualView(binding, requireContext(), requireActivity(), viewModel,
            conversationId.toString(), participant1Id.toString(), viewLifecycleOwner
        )

        //back tipka
        setupBackButtonInbox()

        inboxIndividualView.setupRecyclerView()
        inboxIndividualView.setupMessageSending()
        inboxIndividualView.setupRealtimeUpdates()

        lifecycleScope.launch {
            inboxIndividualView.fetchMessages()
        }


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

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return InboxIndividualViewModel(MessageRepository()) as T
            }
        }).get(InboxIndividualViewModel::class.java)
    }

    fun setupBackButtonInbox() {
        binding.back.setOnClickListener {

            repository.clearUnreadCount(conversationId.toString())

            parentFragmentManager.popBackStack()

        }
    }
}