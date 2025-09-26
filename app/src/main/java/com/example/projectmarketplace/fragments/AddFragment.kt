package com.example.projectmarketplace.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectmarketplace.databinding.FragmentAddBinding
import com.example.projectmarketplace.fragments.base.BaseFragment
import com.example.projectmarketplace.repositories.AddRepository
import com.example.projectmarketplace.viewModels.AddViewModel
import com.example.projectmarketplace.views.AddView
import android.net.Uri
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.projectmarketplace.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class AddFragment : BaseFragment<FragmentAddBinding>() {

    val categories = listOf("Electronics", "Accessories", "Vehicles")
    val conditions = listOf("New", "Like new", "Used")
     val permissionDenied = "Permission denied"
     val loading = "Fetching location..."
    val tryAgain = "Try again"
    val error = "Error"
    val unknownLocation = "Unknown location"
    private lateinit var addView: AddView
    private lateinit var viewModel: AddViewModel
    private var selectedImageUri: Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.imagePreview.setImageURI(it)
            binding.removeImageButton.visibility = View.VISIBLE
            binding.selectImageButton.visibility = View.GONE
        }
    }


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddBinding {
        return FragmentAddBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                getCurrentLocation()
            } else {
                binding.locationButton.text = permissionDenied
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view,savedInstanceState)

        viewModelInit()

        addView = AddView(binding, requireContext(), lifecycleOwner = viewLifecycleOwner, viewModel)

        addView.setupCategoryDropdown(categories)
        addView.setupConditionDropdown(conditions)

        binding.selectImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.removeImageButton.setOnClickListener {
            removeSelectedImage()
        }

        binding.saveButton.setOnClickListener {
            addView.handleSaveButtonClick(selectedImageUri, currentLocation)

        }

        binding.locationButton.setOnClickListener {
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION

        val hasFineLocation = ContextCompat.checkSelfPermission(
            requireContext(), fineLocationPermission
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            requireContext(), coarseLocationPermission
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFineLocation && hasCoarseLocation) {
            getCurrentLocation()
        } else {
            permissionLauncher.launch(arrayOf(fineLocationPermission, coarseLocationPermission))
        }
    }

    private fun removeSelectedImage() {
        selectedImageUri = null
        binding.imagePreview.setImageResource(android.R.drawable.ic_menu_gallery)
        binding.removeImageButton.visibility = View.GONE
        binding.selectImageButton.visibility = View.VISIBLE
    }


    @SuppressLint("SetTextI18n")
    private fun getCurrentLocation() {
        try {
            binding.locationButton.text = loading
            binding.locationButton.isEnabled = false

            val locationTask: Task<Location> = fusedLocationClient.lastLocation
            locationTask.addOnSuccessListener { location ->
                location?.let {
                    currentLocation = it

                    getCityNameFromLocation(it.latitude, it.longitude) { cityName ->
                        requireActivity().runOnUiThread {
                            binding.locationButton.text = "Located in $cityName \uD83D\uDCCD"
                            binding.locationButton.setBackgroundColor(
                                ContextCompat.getColor(requireContext(), R.color.button2)
                            )
                            binding.locationButton.isEnabled = true


                        }
                    }

                } ?: run {
                    binding.locationButton.text = tryAgain

                    binding.locationButton.isEnabled = true

                }
            }.addOnFailureListener { exception ->
                binding.locationButton.text = error

                binding.locationButton.isEnabled = true

            }
        } catch (e: SecurityException) {
            binding.locationButton.text = permissionDenied

            binding.locationButton.isEnabled = true

        }
    }

    private fun getCityNameFromLocation(latitude: Double, longitude: Double, callback: (String) -> Unit) {

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0]

                    val cityName = when {
                        address.locality != null -> address.locality // grad
                        address.subAdminArea != null -> address.subAdminArea // podrucje
                        address.adminArea != null -> address.adminArea // regija
                        address.countryName != null -> address.countryName // drzava
                        else -> unknownLocation
                    }

                    callback(cityName)
                } else {
                    callback(unknownLocation)
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun viewModelInit(){
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AddViewModel(AddRepository()) as T
            }
        }).get(AddViewModel::class.java)
    }

}

