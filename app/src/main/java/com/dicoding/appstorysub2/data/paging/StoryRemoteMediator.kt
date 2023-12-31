package com.dicoding.appstorysub2.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.appstorysub2.data.local.entity.RemoteKeys
import com.dicoding.appstorysub2.data.local.entity.StoryItemEntity
import com.dicoding.appstorysub2.data.local.room.StoryDatabase
import com.dicoding.appstorysub2.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
) : RemoteMediator<Int, StoryItemEntity>() {

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryItemEntity>,
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeysClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeysForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(remoteKeys != null)
                nextKey
            }
        }

        return try {
            val result = apiService.getAllStory(page, state.config.pageSize)
            val endOfPaginationReached = result.listStory.isEmpty()
            val storyEntities = result.listStory.map {
                StoryItemEntity(
                    id = it.id,
                    name = it.name,
                    photoUrl = it.photoUrl,
                    createdAt = it.createdAt,
                    description = it.description,
                    lat = it.lat,
                    lon = it.lon,
                )
            }

            database.run {
                withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        remoteKeysDao().deleteRemoteKeys()
                        storyDao().deleteAll()
                    }
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = result.listStory.map {
                        RemoteKeys(it.id, prevKey = prevKey, nextKey = nextKey)
                    }

                    remoteKeysDao().insertAll(keys)
                    storyDao().insertStories(storyEntities)
                }
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, StoryItemEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeys(data.id)
        }
    }

    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, StoryItemEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeys(data.id)
        }
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, StoryItemEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeys(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}