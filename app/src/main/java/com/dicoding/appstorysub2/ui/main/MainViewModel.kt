package com.dicoding.appstorysub2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagingData
import com.dicoding.appstorysub2.data.local.entity.StoryItemEntity
import com.dicoding.appstorysub2.data.repository.auth.AuthRepository
import com.dicoding.appstorysub2.data.repository.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val refresh = MutableLiveData<Unit>()

    init {
        refresh()
    }

    fun getAllStory(): LiveData<PagingData<StoryItemEntity>> {
        return refresh.switchMap {
            storyRepository.getAllStory().asLiveData()
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun refresh() {
        refresh.value = Unit
    }
}