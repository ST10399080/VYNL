package com.example.vynl.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tier_list_items",
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = ["album_id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["songId"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["albumId"]),
        Index(value = ["songId"]),
        Index(value = ["userId", "itemType"]),
        Index(value = ["userId", "tier"])
    ]
)
data class TierListItemEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tierListItemId")
    val tierListItemId: Long,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "albumId")
    val albumId: Long?,

    @ColumnInfo(name = "songId")
    val songId: Long?,

    @ColumnInfo(name = "itemType")
    val itemType: String,

    @ColumnInfo(name = "tier")
    val tier: String,

    @ColumnInfo(name = "position")
    val position: Int,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long
)