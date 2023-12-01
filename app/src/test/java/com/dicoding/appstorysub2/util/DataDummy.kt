package com.dicoding.appstorysub2.util

import com.dicoding.appstorysub2.data.local.entity.StoryItemEntity

object DataDummy {
    fun generateDummyStoryEntity(): List<StoryItemEntity> {
        return (0..100).map {
            StoryItemEntity(
                "$it",
                "Name $it",
                "ImageAvatar $it",
                "CreatedAt $it",
                "Description $it",
                null,
                null,
            )
        }
    }
}