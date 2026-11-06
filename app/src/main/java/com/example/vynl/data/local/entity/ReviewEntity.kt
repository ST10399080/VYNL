package com.example.vynl.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
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
        Index(value = ["userId", "albumId"], unique = true)
    ]
)
data class ReviewEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reviewId")
    val reviewId: Long,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "albumId")
    val albumId: Long,

    @ColumnInfo(name = "reviewText")
    val reviewText: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long
)