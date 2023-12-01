package com.dicoding.appstorysub2.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.appstorysub2.data.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoutingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    fun getLoginState() = authRepository.getLoginState().asLiveData()
}