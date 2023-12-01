package com.dicoding.appstorysub2.data.remote.request

import android.location.Location
import java.io.File

data class AddStoryRequest(
    val photo: File,
    val description: String,
    val location: Location? = null,
)