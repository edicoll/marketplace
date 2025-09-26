package com.example.projectmarketplace.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectmarketplace.adapters.SearchAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentCategoriesBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.views.CategoriesView


class CategoriesFragment : BaseFragment<FragmentCategoriesBinding>() {

    private lateinit var category: String
    private var items: List<Item> = emptyList()
    private lateinit var adapter: SearchAdapter
    private lateinit var categoriesView: CategoriesView


    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCategoriesBinding {
        return FragmentCategoriesBinding.inflate(inflater, container, false)
    }

    //konfiguracija kreiranog viewa
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category = arguments?.getString(categoryKey) ?: "default_value"
        items = arguments?.getParcelableArrayList<Item>(itemKey, Item::class.java) ?: emptyList()

        binding.title.text = category // prikaz koja je kategorija
        binding.searchRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        if (items.isEmpty()){
            binding.noResult.visibility = View.VISIBLE
        }

        adapter = SearchAdapter(
            requireActivity(),
            items
        )
        binding.searchRecyclerView.adapter = adapter  //na recycleView se postavlja kreirano u adapteru



        categoriesView = CategoriesView(binding, requireActivity(), items, adapter)

        setupBackButton(binding.back)

        categoriesView.setupDropdown()

        categoriesView.setupRadiusSearch()


    }

    companion object {
        fun newInstance(category: String, items: List<Item>): CategoriesFragment {
            return CategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(categoryKey, category)
                    putParcelableArrayList(itemKey, ArrayList(items))
                }
            }
        }

    }

}