package com.example.vynl.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.vynl.data.local.entity.TierListItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TierListItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTierListItem(item: TierListItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTierListItems(
        items: List<TierListItemEntity>
    )

    @Update
    suspend fun updateTierListItem(item: TierListItemEntity)

    @Delete
    suspend fun deleteTierListItem(item: TierListItemEntity)

    @Query(
        "SELECT * FROM tier_list_items " +
                "WHERE userId = :userId " +
                "ORDER BY tier ASC, position ASC"
    )
    fun getUserTierListItems(
        userId: String
    ): Flow<List<TierListItemEntity>>

    @Query(
        "SELECT * FROM tier_list_items " +
                "WHERE userId = :userId AND tier = :tier " +
                "ORDER BY position ASC"
    )
    fun getItemsByTier(
        userId: String,
        tier: String
    ): Flow<List<TierListItemEntity>>

    @Query(
        "SELECT * FROM tier_list_items " +
                "WHERE userId = :userId AND itemType = :itemType " +
                "ORDER BY tier ASC, position ASC"
    )
    fun getItemsByType(
        userId: String,
        itemType: String
    ): Flow<List<TierListItemEntity>>

    @Query("DELETE FROM tier_list_items WHERE userId = :userId")
    suspend fun deleteUserTierListItems(userId: String)
}