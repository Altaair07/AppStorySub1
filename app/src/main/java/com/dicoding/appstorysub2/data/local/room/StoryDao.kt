package com.dicoding.appstorysub2.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.appstorysub2.data.local.entity.StoryItemEntity

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStories(stories: List<StoryItemEntity>)

    @Query("SELECT * FROM story ORDER BY createdAt DESC")
    fun getAllStories(): PagingSource<Int, StoryItemEntity>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}