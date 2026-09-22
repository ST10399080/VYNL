package com.example.vynl.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "songs",
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
        Index(value = ["lastFmTrackId"], unique = true)
    ]
)
data class SongEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "songId")
    val songId: Long = 0,

    @ColumnInfo(name = "lastFmTrackId")
    val lastFmTrackId: String,

    @ColumnInfo(name = "albumId")
    val albumId: Long?,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "artistName")
    val artistName: String,

    @ColumnInfo(name = "duration")
    val duration: Int?,

    @ColumnInfo(name = "trackNumber")
    val trackNumber: Int?,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String?,

    @ColumnInfo(name = "streamable")
    val streamable: Boolean = false,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis()
)