package com.example.vynl.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vynl.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Update
    suspend fun updateReview(review: ReviewEntity)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)

    @Query(
        "SELECT * FROM reviews " +
                "WHERE userId = :userId AND albumId = :albumId"
    )
    suspend fun getReview(
        userId: String,
        albumId: Long
    ): ReviewEntity?

    @Query("SELECT * FROM reviews WHERE userId = :userId")
    fun getUserReviews(userId: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE albumId = :albumId")
    fun getAlbumReviews(albumId: Long): Flow<List<ReviewEntity>>

    @Query(
        "SELECT * FROM reviews " +
                "WHERE userId = :userId AND albumId = :albumId"
    )
    fun observeReview(
        userId: String,
        albumId: Long
    ): Flow<ReviewEntity?>

    @Query("DELETE FROM reviews WHERE userId = :userId")
    suspend fun deleteUserReviews(userId: String)
}