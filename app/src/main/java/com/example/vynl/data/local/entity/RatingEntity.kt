package com.example.vynl.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ratings",
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = ["album_id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["albumId"]),
        Index(value = ["userId", "albumId"], unique = true)
    ]
)
data class RatingEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ratingId")
    val ratingId: Long = 0,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "albumId")
    val albumId: Long,

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long = System.currentTimeMillis()
)