package com.example.projectmarketplace.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectmarketplace.adapters.SearchAdapter
import com.example.projectmarketplace.databinding.FragmentSearchBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.ItemRepository
import com.example.projectmarketplace.services.ImageSearchService
import com.example.projectmarketplace.services.VisionService
import com.example.projectmarketplace.viewModels.SearchViewModel
import com.example.projectmarketplace.views.SearchView
import kotlinx.coroutines.launch
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.projectmarketplace.R
import com.example.projectmarketplace.data.Item
import java.io.InputStream
import kotlin.collections.isNotEmpty


class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    //view binding je mehanizam koji generira klasu koja omogućuje pristup elementima iz XML layouta
    // generira klasu iz layouta npr.FragmentSearchBinding iz fragment_search.xml, ta klsa sadrži refernce na vieowe

    private lateinit var adapter: SearchAdapter
    private lateinit var searchView: SearchView
    private lateinit var viewModel: SearchViewModel
    val categories = listOf(
        "Electronics",
        "Accessories",
        "Vehicles"
    )
    private var photoSearch = "Photo Search"
    private var error = "Error"
    private lateinit var imageSearchService: ImageSearchService
    private lateinit var visionService: VisionService
    lateinit var itemRepository: ItemRepository

    // Registriraj launcher za odabir slike
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { handleSelectedImage(it) }
    }


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelInit()

        binding.searchRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = SearchAdapter(
            requireActivity(),
            emptyList()
        )

        searchView = SearchView(binding, requireActivity(), adapter, viewModel)

        lifecycleScope.launch {
            searchView.fetchItems()
        }

        binding.searchRecyclerView.adapter = adapter  //na recycleView se postavlja kreirano u adapteru

        searchView.setupSearchBar()

        searchView.setupDropdown()

        searchView.setupCategoryClickListener(categories)

        // Inicijaliziraj servise
        visionService = VisionService(requireContext())
        itemRepository = ItemRepository()

        imageSearchService = ImageSearchService(itemRepository).apply {
            setVisionService(visionService)
        }

        setupImageSearchButton()

    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(ItemRepository()) as T
            }
        }).get(SearchViewModel::class.java)
    }

    private fun setupImageSearchButton() {
        binding.imageSearch.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }
    }

    private fun handleSelectedImage(uri: Uri) {
        lifecycleScope.launch {
            try {
                showLoading(true)

                // pretvorba URI u Bitmap
                val bitmap = uriToBitmap(uri)
                if (bitmap != null) {
                    // pretraga
                    val results = imageSearchService.searchByImage(bitmap)
                    showSearchResults(results)
                } else {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()

            } finally {
                showLoading(false)
            }
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
            binding.imageSearchButton.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.imageSearchButton.isEnabled = true
        }
    }

    private fun showSearchResults(results: List<Item>) {
            val fragment = CategoriesFragment.newInstance(photoSearch, results)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.flFragment, fragment)
                ?.addToBackStack(null)
                ?.commit()
    }



}

