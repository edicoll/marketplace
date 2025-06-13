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
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentHomeIndividualBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ConversationRepository
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.viewModels.ItemViewModel
import com.example.projectmarketplace.viewModels.SearchViewModel
import com.example.projectmarketplace.views.ItemView
import kotlinx.coroutines.launch


class ItemFragment : BaseFragment<FragmentHomeIndividualBinding>() {

    private lateinit var item: Item
    private val itemNotFound = "Item not found"
    private lateinit var itemView: ItemView
    private lateinit var viewModel: ItemViewModel
    val itemRepository = ItemRepository()


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeIndividualBinding {
        return FragmentHomeIndividualBinding.inflate(inflater, container, false)
    }

    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInit()
        //dohvaćanje proslijeđenih podataka, getParceable je isto kao pojedinačno
        //dohvaća za cijeli objekt Item
        item = arguments?.getParcelable(itemKey, Item::class.java) ?: throw IllegalStateException(itemNotFound)

        itemView = ItemView(binding, requireContext(), item, itemRepository,
            lifecycleOwner = viewLifecycleOwner, requireActivity(), viewModel,
            )

        //back tipka
        setupBackButton(binding.back)

        itemView.setFavItem()

        binding.buttonContact.setOnClickListener {
            lifecycleScope.launch {
                itemView.contactSeller()
            }
        }

        itemView.bind()

    }

    companion object {
        fun newInstance(item: Item): ItemFragment {
            return ItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(itemKey, item)
                }
            }
        }
    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ItemViewModel(ConversationRepository()) as T
            }
        }).get(ItemViewModel::class.java)
    }
}