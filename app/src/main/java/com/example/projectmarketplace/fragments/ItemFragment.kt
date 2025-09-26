package com.example.projectmarketplace.fragments

import android.location.Geocoder
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
import com.example.projectmarketplace.views.ItemView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import java.util.Locale


class ItemFragment : BaseFragment<FragmentHomeIndividualBinding>(), OnMapReadyCallback  {

    private lateinit var item: Item
    private val itemNotFound = "Item not found"
    private lateinit var itemView: ItemView
    private lateinit var viewModel: ItemViewModel
    private lateinit var googleMap: GoogleMap
    private var mapView: com.google.android.gms.maps.MapView? = null
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

        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        itemView = ItemView(binding, requireContext(), item, itemRepository,
            lifecycleOwner = viewLifecycleOwner, requireActivity(), viewModel,
            )

        //back tipka
        setupBackButton(binding.back)

        itemView.setFavItem()

        lifecycleScope.launch {
            itemView.upgradeRecentlyViewed(item.id)
        }

        binding.buttonContact.setOnClickListener {
            lifecycleScope.launch {
                itemView.contactSeller()
            }
        }

        binding.buttonBuy.setOnClickListener {
            lifecycleScope.launch {
                itemView.buyItem()
            }
        }

        itemView.bind()

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Postavi marker na lokaciju proizvoda
        item.latitude?.let { lat ->
            item.longitude?.let { lng ->
                val itemLocation = LatLng(lat, lng)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(itemLocation)
                        .title("Lokacija proizvoda")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itemLocation, 12f))

                // Ažuriraj tekst lokacije
                updateLocationText(lat, lng)
            }
        } ?: run {
            // Ako nema lokacije
            binding.locationText.text = "Lokacija nije dostupna"
            binding.mapView.visibility = View.GONE
        }
    }

    private fun updateLocationText(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            try {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val city = address.locality ?: address.subAdminArea ?: address.adminArea
                    binding.locationText.text = city ?: "Nepoznata lokacija"
                } else {
                    binding.locationText.text = "Lokacija: ${String.format("%.4f", latitude)}, ${String.format("%.4f", longitude)}"
                }
            } catch (e: Exception) {
                binding.locationText.text = "Lokacija: ${String.format("%.4f", latitude)}, ${String.format("%.4f", longitude)}"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
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


