package com.dicoding.appstorysub2.data.repository.auth

import com.dicoding.appstorysub2.data.remote.response.LoginResultResponse
import com.dicoding.appstorysub2.data.remote.response.RegisterResponse
import com.dicoding.appstorysub2.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Result<LoginResultResponse>>
    fun register(name: String, email: String, password: String): Flow<Result<RegisterResponse>>
    fun getLoginState(): Flow<Boolean>
    fun logout()
}