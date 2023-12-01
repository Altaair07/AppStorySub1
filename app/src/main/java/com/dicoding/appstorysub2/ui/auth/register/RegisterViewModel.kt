package com.dicoding.appstorysub2.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.appstorysub2.data.remote.response.RegisterResponse
import com.dicoding.appstorysub2.data.repository.auth.AuthRepository
import com.dicoding.appstorysub2.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> {
        return authRepository.register(name, email, password).asLiveData()
    }
}