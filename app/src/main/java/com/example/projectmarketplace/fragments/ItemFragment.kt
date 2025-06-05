package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentHomeIndividualBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.views.ItemView


class ItemFragment : BaseFragment<FragmentHomeIndividualBinding>() {

    private lateinit var item: Item
    private val itemNotFound = "Item not found"
    private lateinit var itemView: ItemView

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

        //dohvaćanje proslijeđenih podataka, getParceable je isto kao pojedinačno
        //dohvaća za cijeli objekt Item
        item = arguments?.getParcelable(itemKey, Item::class.java) ?: throw IllegalStateException(itemNotFound)

        itemView = ItemView(binding, requireContext(), item)

        //back tipka
        setupBackButton(binding.back)

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
}