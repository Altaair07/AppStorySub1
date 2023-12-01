package com.dicoding.appstorysub2.ui.mapsstory

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.dicoding.appstorysub2.R
import com.dicoding.appstorysub2.data.remote.response.StoryItemResponse
import com.dicoding.appstorysub2.databinding.ActivityMapsStoryBinding
import com.dicoding.appstorysub2.util.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsStoryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsStoryBinding
    private val viewModel by viewModels<MapsStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setupMapStyle()

        viewModel.getAllStoryWithLocation().observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showLoading(false)
                    Snackbar.make(binding.root, result.message.toString(), Snackbar.LENGTH_LONG).show()
                }
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showMarkers(result.data)
                    showLoading(false)
                }
            }
        }
    }

    private fun showMarkers(stories: List<StoryItemResponse>) {
        val firstStory = stories.firstOrNull() ?: return
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
            LatLng(firstStory.lat ?: return,  firstStory.lon ?: return), 10f
        )
        map.animateCamera(cameraUpdate)

        stories.forEach {
            if (it.lat != null && it.lon != null) {
                val latLng = LatLng(it.lat, it.lon)
                map.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(it.name)
                )
            }
        }
    }

    private fun setupMapStyle() {
        try {
            val isSuccess = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_style)
            )
            if (!isSuccess) Log.e("MapsStoryActivity", "setMapStyle: Parsing Failed.")
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            pbLoading.isVisible = isLoading
        }
    }
}