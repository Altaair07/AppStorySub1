package com.dicoding.appstorysub2.data.repository.story

import androidx.paging.PagingData
import com.dicoding.appstorysub2.data.local.entity.StoryItemEntity
import com.dicoding.appstorysub2.data.remote.request.AddStoryRequest
import com.dicoding.appstorysub2.data.remote.response.AddStoryResponse
import com.dicoding.appstorysub2.data.remote.response.StoryItemResponse
import com.dicoding.appstorysub2.util.Result
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getAllStory(): Flow<PagingData<StoryItemEntity>>
    fun getAllStoryWithLocation(): Flow<Result<List<StoryItemResponse>>>
    fun getDetailStory(id: String): Flow<Result<StoryItemResponse>>
    fun addNewStory(addStoryRequest: AddStoryRequest): Flow<Result<AddStoryResponse>>
}