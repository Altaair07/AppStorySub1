package com.dicoding.appstorysub2.ui.addstory

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.dicoding.appstorysub2.R
import com.dicoding.appstorysub2.databinding.ActivityAddStoryBinding
import com.dicoding.appstorysub2.util.Result
import com.dicoding.appstorysub2.util.getImageUri
import com.dicoding.appstorysub2.util.reduceFileImage
import com.dicoding.appstorysub2.util.uriToFile
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAddStoryBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<AddStoryViewModel>()

    private var currentUri: Uri? = null
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private lateinit var locationRequest: LocationRequest
    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        with(binding.contentAddStory) {
            btnCamera.setOnClickListener {
                currentUri = getImageUri(this@AddStoryActivity)
                launcherCamera.launch(currentUri)
            }
            btnGallery.setOnClickListener {
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            buttonAdd.setOnClickListener {
                addNewStory()
            }
            cbGps.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    getMyLastLocation()
                    createLocationRequest()
                } else {
                    location = null
                }
            }
        }
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            and checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                this.location = location
                if (location == null) {
                    Snackbar.make(binding.root,
                        getString(R.string.location_null),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            launcherPermission.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(1).apply {
            setIntervalMillis(TimeUnit.SECONDS.toMillis(1))
            setMaxUpdateDelayMillis(TimeUnit.SECONDS.toMillis(1))
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }.build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)

        client.checkLocationSettings(builder.build())
            .addOnSuccessListener { getMyLastLocation() }
            .addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(IntentSenderRequest.Builder(e.resolution).build())
                    } catch (e: IntentSender.SendIntentException) {
                        Snackbar.make(binding.root, "Fail", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun addNewStory() {
        currentUri?.let {
            val imageFile = uriToFile(it, this).reduceFileImage()
            val description = binding.contentAddStory.edAddDescription.text.toString().trim()

            if (description.isBlank()) {
                Snackbar.make(binding.root, "Deskripsi wajib diisi", Snackbar.LENGTH_SHORT).show()
                return
            }

            viewModel.addNewStory(imageFile, description, location).observe(this) { result ->
                when (result) {
                    is Result.Error -> {
                        showLoading(false)
                        Snackbar.make(binding.root, "${result.message}", Snackbar.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        Snackbar.make(binding.root, "${result.data.message}", Snackbar.LENGTH_LONG).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
        } ?: run {
            Snackbar.make(binding.root, "Gambar tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            pbLoading.isVisible = isLoading
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let {
            currentUri = it
            binding.contentAddStory.ivStory.setImageURI(currentUri)
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            contentResolver.takePersistableUriPermission(it, flag)
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            binding.contentAddStory.ivStory.setImageURI(currentUri)
        }
    }

    private val launcherPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
        }
    }

    private val resolutionLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> Toast.makeText(
                this,
                "Tunggu...",
                Toast.LENGTH_SHORT
            ).show()
            Activity.RESULT_CANCELED -> Toast.makeText(
                this,
                "Gagal mendapatkan lokasi",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}