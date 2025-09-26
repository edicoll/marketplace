package com.example.projectmarketplace.views

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentActivity
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.SearchAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentSearchBinding
import com.example.projectmarketplace.fragments.CategoriesFragment
import com.example.projectmarketplace.viewModels.SearchViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlin.text.equals
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.example.projectmarketplace.utils.LocationUtils
import kotlinx.coroutines.launch


class SearchView(private val binding: FragmentSearchBinding,
                 private val activity: FragmentActivity,
                 private val adapter: SearchAdapter,
                 private var viewModel: SearchViewModel
) {

    private var selectedFilter: String = "Default"
    private var currentDisplayedItems: List<Item> = emptyList()
    private val default = "Default"
    private val newest_first = "Newest first"
    private val price_low_to_hight = "Price: low to high"
    private val price_hight_to_low = "Price: high to low"
    private val categoryViews = mapOf(
        "electronics" to binding.electronics,
        "accessories" to binding.accessories,
        "vehicles" to binding.vehicles
    )
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    private var currentLocation: LatLng? = null
    private var isRadiusSearchActive = false


    //možda bi trebalo za kategorije nabravit fetchItemsCategory
    suspend fun fetchItems() {
        viewModel.getItems() // ovo automatski sprema originalne iteme
        performSearch(binding.searchBar.text.toString()) //potrebno kada se navigira između tabova da ostane search
    }
    // funkcija za postavljanje spinnera
    fun setupDropdown(){
        val spinner = binding.filtering // dohvaća referencu na spinner
        ArrayAdapter.createFromResource( // kreira se adapter koji postavlja filtere i dizajn
            activity,
            R.array.filter_options,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item) //layout za prikaz padajućeg izbornika
            spinner.adapter = adapter //postavlja kreirani adapter na spinner
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedFilter = parent.getItemAtPosition(position).toString()
                val sortedItems = applyFilter(currentDisplayedItems)
                adapter.updateItems(sortedItems)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // funkcijaza postavljanje search bara i viewa
    fun setupSearchBar(){
        // postavljanje SearchBara
        binding.searchBar.apply {
            setOnClickListener {
                binding.searchView.show()  //prikazuje se searchView kada se klikne na searchBar
                //binding.searchView.editText.requestFocus()
                //binding.searchView.visibility = View.VISIBLE
                binding.searchView.requestFocusAndShowKeyboard()  //prikazivanje tastature
            }
        }

        // postavljanje SearchView-a
        binding.searchView.apply {

            editText.setOnEditorActionListener { _, actionId, _ ->  //postavljanje listenera za akciju pretrage na tastaturi
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(text.toString())     //pretraga kada se pritisne search
                    hide()                 //sakrivanje searchViewa nakon pretrage
                    true
                } else {
                    false
                }
            }
            //postavljanje listenera za live search
            editText.doOnTextChanged { text, _, _, _ ->
                //Log.d("Promjena", "Tekst:  ${text} ")
                text?.let { performSearch(it.toString()) } //pretraga se izvršava pri svakoj promjeni teksta
                binding.searchBar.setText(text) //prikaz slova u search baru
            }
        }
    }


    // funkcija za pretraživanje
    fun performSearch(query: String){
        val spinner = binding.filtering
        val categories = binding.categories
        val noResult = binding.noResult

        //provjerava ako je null da traži lokaciju, jer je ona već dana u nekim sučajevima
        if (currentLocation == null){
            activity.lifecycleScope.launch {
                currentLocation = LocationUtils.getCurrentLocation(activity, fusedLocationClient)
            }
        }

        val filteredItems = if (query.length >= 3) {
            val textFilteredList = viewModel.originalItems.filter { item ->
                item.title.contains(query, ignoreCase = true) ||
                        item.description.contains(query, ignoreCase = true)
            }

            // provjera je li radius aktivan
            val finalList = if (isRadiusSearchActive && currentLocation != null) {
                val radius = binding.radiusSeekBar.progress + 10
                textFilteredList.filter { item ->
                    item.latitude != null && item.longitude != null &&
                            LocationUtils.calculateDistance(
                                currentLocation!!.latitude,
                                currentLocation!!.longitude,
                                item.latitude,
                                item.longitude
                            ) <= radius
                }
            } else {
                textFilteredList
            }

            // upravljanje vizibilnošću elemenata
            spinner.visibility = if (finalList.isNotEmpty()) View.VISIBLE else View.GONE
            categories.visibility = View.GONE
            noResult.visibility = if (finalList.isEmpty()) View.VISIBLE else View.GONE

            applyFilter(finalList)

        } else {
            spinner.visibility = View.GONE
            categories.visibility = View.VISIBLE
            noResult.visibility = View.GONE
            binding.filtering.setSelection(0)
            emptyList<Item>() // prazna lista se varća
        }

        currentDisplayedItems = filteredItems
        adapter.updateItems(filteredItems) // adapter se ažurira s odabranim rezultatima
    }

    // funkcija kada se filtrira
    private fun applyFilter(list: List<Item>): List<Item> {
        return when (selectedFilter) {
            default -> list.sortedByDescending { it.createdAt }
            newest_first -> list.sortedByDescending { it.createdAt }
            price_low_to_hight -> list.sortedBy { it.price }
            price_hight_to_low -> list.sortedByDescending { it.price }
            else -> list
        }
    }

    fun setupCategoryClickListener(categories: List<String>) {
        categories.forEach { category ->

            val view = categoryViews[category.lowercase()]

            view?.setOnClickListener {
                val filteredItems = viewModel.originalItems.filter { item ->
                    item.category.equals(category, ignoreCase = true)
                }

                val fragment = CategoriesFragment.newInstance(category, filteredItems)
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    init {
        setupRadiusSearch()
    }

    private fun setupRadiusSearch() {
        // postvalja se SeekBar
        binding.radiusSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val radius = progress + 10 // 10-100 km
                binding.radiusText.text = "$radius km"

                // osvježava pretragu kada se radius mijenja
                if (fromUser && isRadiusSearchActive && currentLocation != null) {
                    performSearch(binding.searchBar.text.toString())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Gumb za radius pretragu
        binding.radiusSearchButton.setOnClickListener {
            if (isRadiusSearchActive) {
                disableRadiusSearch()
            } else {
                enableRadiusSearch()
            }
        }
    }

    private fun enableRadiusSearch() {

        isRadiusSearchActive = true
        binding.radiusSearchButton.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary))
        binding.radiusLayout.visibility = View.VISIBLE

        // Dohvati trenutnu lokaciju
        activity.lifecycleScope.launch {

            currentLocation = LocationUtils.getCurrentLocation(activity, fusedLocationClient)

            if (currentLocation != null) {

                performSearch(binding.searchBar.text.toString())
            } else {
                requestLocationPermissions()
            }
        }
    }


    private fun disableRadiusSearch() {

        isRadiusSearchActive = false
        binding.radiusSearchButton.setColorFilter(ContextCompat.getColor(activity, android.R.color.darker_gray))
        binding.radiusLayout.visibility = View.GONE
        // osviježavanje performSearcha
        performSearch(binding.searchBar.text.toString())
    }


    private fun requestLocationPermissions() {
        // traženje dozvola za lokaciju
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
