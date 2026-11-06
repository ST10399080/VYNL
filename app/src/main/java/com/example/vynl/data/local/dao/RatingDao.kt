package com.example.vynl.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vynl.data.local.entity.RatingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRating(rating: RatingEntity)

    @Update
    suspend fun updateRating(rating: RatingEntity)

    @Delete
    suspend fun deleteRating(rating: RatingEntity)

    @Query(
        "SELECT * FROM ratings " +
                "WHERE userId = :userId AND albumId = :albumId"
    )
    suspend fun getRating(
        userId: String,
        albumId: Long
    ): RatingEntity?

    @Query("SELECT * FROM ratings WHERE userId = :userId")
    fun getUserRatings(userId: String): Flow<List<RatingEntity>>

    @Query("SELECT * FROM ratings WHERE albumId = :albumId")
    fun getAlbumRatings(albumId: Long): Flow<List<RatingEntity>>

    @Query(
        "SELECT * FROM ratings " +
                "WHERE userId = :userId AND albumId = :albumId"
    )
    fun observeRating(
        userId: String,
        albumId: Long
    ): Flow<RatingEntity?>

    @Query("DELETE FROM ratings WHERE userId = :userId")
    suspend fun deleteUserRatings(userId: String)
}