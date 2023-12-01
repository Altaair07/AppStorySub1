package com.dicoding.appstorysub2.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.appstorysub2.data.remote.response.LoginResultResponse
import com.dicoding.appstorysub2.data.repository.auth.AuthRepository
import com.dicoding.appstorysub2.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    fun login(email: String, password: String): LiveData<Result<LoginResultResponse>> {
        return authRepository.login(email, password).asLiveData()
    }
}