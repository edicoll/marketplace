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
import com.example.projectmarketplace.databinding.FragmentEditItemBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.viewModels.EditItemViewModel
import com.example.projectmarketplace.views.EditItemView
import kotlinx.coroutines.launch

class EditItemFragment : BaseFragment<FragmentEditItemBinding>() {

    private lateinit var item: Item
    private val itemNotFound = "Item not found"
    private lateinit var itemView: EditItemView
    private lateinit var viewModel: EditItemViewModel
    val itemRepository = ItemRepository()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditItemBinding {
        return FragmentEditItemBinding.inflate(inflater, container, false)
    }

    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInit()
        //dohvaćanje proslijeđenih podataka, getParceable je isto kao pojedinačno
        //dohvaća za cijeli objekt Item
        item = arguments?.getParcelable(itemKey, Item::class.java) ?: throw IllegalStateException(itemNotFound)

        itemView = EditItemView(binding, requireContext(), item, itemRepository,
            lifecycleOwner = viewLifecycleOwner, requireActivity(), viewModel,
        )

        //back tipka
        setupBackButton(binding.back)


        binding.buttonDelete.setOnClickListener {
            lifecycleScope.launch {
                itemView.deleteItem()
            }
        }

        itemView.bind()

    }

    companion object {
        fun newInstance(item: Item): EditItemFragment {
            return EditItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(itemKey, item)
                }
            }
        }
    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EditItemViewModel(ItemRepository()) as T
            }
        }).get(EditItemViewModel::class.java)
    }
}