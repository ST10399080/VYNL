package com.example.vynl.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rankings",
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = ["album_id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["albumId"]),
        Index(value = ["userId", "albumId"], unique = true),
        Index(value = ["userId", "rankPosition"], unique = true)
    ]
)
data class RankingEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rankingId")
    val rankingId: Long,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "albumId")
    val albumId: Long,

    @ColumnInfo(name = "rankPosition")
    val rankPosition: Int,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long
)