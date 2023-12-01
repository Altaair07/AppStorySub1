package com.dicoding.appstorysub2.data.remote

import com.dicoding.appstorysub2.data.remote.request.AddStoryRequest
import com.dicoding.appstorysub2.data.remote.response.AddStoryResponse
import com.dicoding.appstorysub2.data.remote.response.AllStoryResponse
import com.dicoding.appstorysub2.data.remote.response.DetailStoryResponse
import com.dicoding.appstorysub2.data.remote.response.LoginResponse
import com.dicoding.appstorysub2.data.remote.response.RegisterResponse
import com.dicoding.appstorysub2.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun getAllStory(page: Int? = null, size: Int? = null, location: Int = 0): AllStoryResponse {
        return apiService.getAllStory(page, size, location)
    }

    suspend fun getDetailStory(id: String): DetailStoryResponse {
        return apiService.getDetailStory(id)
    }

    suspend fun addNewStory(addStoryRequest: AddStoryRequest): AddStoryResponse {
        val description = addStoryRequest.description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = addStoryRequest.photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            addStoryRequest.photo.name,
            requestImageFile
        )

        var latitude: RequestBody? = null
        var longitude: RequestBody? = null

        addStoryRequest.location?.let {
            latitude = it.latitude.toString().toRequestBody("text/plain".toMediaType())
            longitude = it.longitude.toString().toRequestBody("text/plain".toMediaType())
        }

        return apiService.addNewStory(imageMultipart, description, latitude, longitude)
    }
}