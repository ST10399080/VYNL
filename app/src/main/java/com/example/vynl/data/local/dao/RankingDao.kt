package com.example.vynl.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vynl.data.local.entity.RankingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RankingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRanking(ranking: RankingEntity)

    @Update
    suspend fun updateRanking(ranking: RankingEntity)

    @Delete
    suspend fun deleteRanking(ranking: RankingEntity)

    @Query(
        "SELECT * FROM rankings " +
                "WHERE userId = :userId " +
                "ORDER BY rankPosition ASC"
    )
    fun getUserRankings(userId: String): Flow<List<RankingEntity>>

    @Query(
        "SELECT * FROM rankings " +
                "WHERE userId = :userId AND albumId = :albumId"
    )
    suspend fun getRanking(
        userId: String,
        albumId: Long
    ): RankingEntity?

    @Query(
        "SELECT * FROM rankings " +
                "WHERE userId = :userId AND rankPosition = :rankPosition"
    )
    suspend fun getRankingAtPosition(
        userId: String,
        rankPosition: Int
    ): RankingEntity?

    @Query("DELETE FROM rankings WHERE userId = :userId")
    suspend fun deleteUserRankings(userId: String)
}