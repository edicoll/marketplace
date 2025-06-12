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
import com.example.projectmarketplace.databinding.FragmentInboxBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ConversationRepository
import com.example.projectmarketplace.viewModels.InboxViewModel
import com.example.projectmarketplace.views.InboxView
import kotlinx.coroutines.launch


class InboxFragment : BaseFragment<FragmentInboxBinding>() {


    private lateinit var viewModel: InboxViewModel
    private lateinit var inboxView: InboxView

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInboxBinding {
        return FragmentInboxBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInit()

        inboxView = InboxView(binding, requireContext(), requireActivity(), viewModel)

        inboxView.setupRecyclerView()

        lifecycleScope.launch {
            inboxView.fetchConversations()
        }
    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return InboxViewModel(ConversationRepository()) as T
            }
        }).get(InboxViewModel::class.java)
    }
}
