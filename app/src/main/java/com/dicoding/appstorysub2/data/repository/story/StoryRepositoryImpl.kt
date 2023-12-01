package com.dicoding.appstorysub2.data.repository.story

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicoding.appstorysub2.data.local.entity.StoryItemEntity
import com.dicoding.appstorysub2.data.local.room.StoryDatabase
import com.dicoding.appstorysub2.data.paging.StoryRemoteMediator
import com.dicoding.appstorysub2.data.remote.RemoteDataSource
import com.dicoding.appstorysub2.data.remote.request.AddStoryRequest
import com.dicoding.appstorysub2.data.remote.response.AddStoryResponse
import com.dicoding.appstorysub2.data.remote.response.StoryItemResponse
import com.dicoding.appstorysub2.data.remote.retrofit.ApiService
import com.dicoding.appstorysub2.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase,
) : StoryRepository {

    override fun getAllStory(): Flow<PagingData<StoryItemEntity>> =
        @OptIn(ExperimentalPagingApi::class)
        Pager(
            config = PagingConfig(pageSize = 8),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            },
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService)
        ).flow

    override fun getAllStoryWithLocation(): Flow<Result<List<StoryItemResponse>>> = flow {
        emit(Result.Loading())
        try {
            val result = remoteDataSource.getAllStory(location = 1)
            emit(Result.Success(result.listStory))
        } catch (e: Exception) {
            emit(Result.Error(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun getDetailStory(id: String): Flow<Result<StoryItemResponse>> = flow {
        emit(Result.Loading())
        try {
            val result = remoteDataSource.getDetailStory(id)
            emit(Result.Success(result.story))
        } catch (e: Exception) {
            emit(Result.Error(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun addNewStory(addStoryRequest: AddStoryRequest): Flow<Result<AddStoryResponse>> = flow {
        emit(Result.Loading())
        try {
             val result = remoteDataSource.addNewStory(addStoryRequest)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message))
        }
    }.flowOn(Dispatchers.IO)
}