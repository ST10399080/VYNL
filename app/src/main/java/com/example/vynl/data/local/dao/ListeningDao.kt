package com.example.vynl.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vynl.data.local.entity.ListeningEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListeningDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListening(listening: ListeningEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListenings(items: List<ListeningEntity>)

    @Delete
    suspend fun deleteListening(listening: ListeningEntity)

    @Query(
        "SELECT * FROM listenings " +
                "WHERE userId = :userId " +
                "ORDER BY listenedAt DESC"
    )
    fun getUserListeningHistory(
        userId: String
    ): Flow<List<ListeningEntity>>

    @Query(
        "SELECT * FROM listenings " +
                "WHERE userId = :userId AND songId = :songId " +
                "ORDER BY listenedAt DESC"
    )
    fun getSongListeningHistory(
        userId: String,
        songId: Long
    ): Flow<List<ListeningEntity>>

    @Query(
        "SELECT * FROM listenings " +
                "WHERE userId = :userId " +
                "ORDER BY listenedAt DESC LIMIT :limit"
    )
    fun getRecentListening(
        userId: String,
        limit: Int
    ): Flow<List<ListeningEntity>>

    @Query("DELETE FROM listenings WHERE userId = :userId")
    suspend fun deleteUserListeningHistory(userId: String)
}