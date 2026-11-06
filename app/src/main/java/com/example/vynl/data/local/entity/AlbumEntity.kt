package com.example.vynl.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "albums",
    indices = [
        Index(
            value = ["last_fm_mbid"],
            unique = false
        ),
        Index(
            value = ["artist_name"],
            unique = false
        )
    ]
)
data class AlbumEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "album_id")
    val albumId: Long = 0,

    @ColumnInfo(name = "last_fm_mbid")
    val lastFmMbid: String? = null,

    @ColumnInfo(name = "album_name")
    val albumName: String,

    @ColumnInfo(name = "artist_name")
    val artistName: String,

    @ColumnInfo(name = "artist_mbid")
    val artistMbid: String? = null,

    @ColumnInfo(name = "release_date")
    val releaseDate: String? = null,

    @ColumnInfo(name = "genre")
    val genre: String? = null,

    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "track_count")
    val trackCount: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)