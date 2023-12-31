package com.dicoding.appstorysub2.data.remote.retrofit

import com.dicoding.appstorysub2.data.remote.response.AddStoryResponse
import com.dicoding.appstorysub2.data.remote.response.AllStoryResponse
import com.dicoding.appstorysub2.data.remote.response.DetailStoryResponse
import com.dicoding.appstorysub2.data.remote.response.LoginResponse
import com.dicoding.appstorysub2.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("stories")
    suspend fun getAllStory(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0,
    ): AllStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String,
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody? = null,
        @Part("lon") longitude: RequestBody? = null,
    ): AddStoryResponse
}