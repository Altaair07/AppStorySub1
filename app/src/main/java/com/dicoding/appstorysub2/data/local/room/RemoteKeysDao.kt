package com.dicoding.appstorysub2.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.appstorysub2.data.local.entity.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeys(id: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemoteKeys()
}