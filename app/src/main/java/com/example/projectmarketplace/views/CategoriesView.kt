package com.example.projectmarketplace.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.projectmarketplace.R
import com.example.projectmarketplace.adapters.SearchAdapter
import com.example.projectmarketplace.data.Item
import com.example.projectmarketplace.databinding.FragmentCategoriesBinding
import com.example.projectmarketplace.utils.LocationUtils
import com.example.projectmarketplace.views.SearchView.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class CategoriesView(private val binding: FragmentCategoriesBinding,
                 private val activity: FragmentActivity,
                 private var items: List<Item> = emptyList(),
                 private val adapter: SearchAdapter
) {
    private var selectedFilter: String = "Default"
    private val default = "Default"
    private val newest_first = "Newest first"
    private val price_low_to_hight = "Price: low to high"
    private val price_hight_to_low = "Price: high to low"
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    private var currentLocation: LatLng? = null
    private var isRadiusSearchActive = false
    private var pendingRadiusSearch = false


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
                val sortedItems = applyFilter(items)
                adapter.updateItems(sortedItems)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
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

    private fun performSearch(){
        val radiusItems = if (isRadiusSearchActive && currentLocation != null) {
            val radius = binding.radiusSeekBar.progress + 10
            items.filter { item ->
                item.latitude != null && item.longitude != null &&
                        LocationUtils.calculateDistance(
                            currentLocation!!.latitude,
                            currentLocation!!.longitude,
                            item.latitude,
                            item.longitude
                        ) <= radius
            }
        } else {
            items
        }
        val finalList = applyFilter(radiusItems)

        if (finalList.isEmpty()){
            binding.noResult.visibility = View.VISIBLE
        }else{
            binding.noResult.visibility = View.GONE
        }
        adapter.updateItems(finalList)
    }

     fun setupRadiusSearch() {
        // postvalja se SeekBar
        binding.radiusSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val radius = progress + 10 // 10-100 km
                binding.radiusText.text = "$radius km"

                // osvježava pretragu kada se radius mijenja
                if (fromUser && isRadiusSearchActive && currentLocation != null) {
                    performSearch()
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

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
                // Dozvole su već odobrene, dohvati lokaciju i izvrši pretragu
                activity.lifecycleScope.launch {
                    currentLocation = LocationUtils.getCurrentLocation(activity, fusedLocationClient)
                    performSearch()
                }
            } else {
                // Dozvole nisu odobrene, zatraži ih i postavi zastavicu za čekajuću pretragu
                pendingRadiusSearch = true
                requestLocationPermissions()
            }
        }
}


        private fun disableRadiusSearch() {

        isRadiusSearchActive = false
        binding.radiusSearchButton.setColorFilter(ContextCompat.getColor(activity, android.R.color.darker_gray))
        binding.radiusLayout.visibility = View.GONE
        // osviježavanje performSearcha
        performSearch()
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

}
