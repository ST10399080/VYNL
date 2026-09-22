package com.example.vynl.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "listenings",
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["songId"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["songId"]),
        Index(value = ["userId", "listenedAt"])
    ]
)
data class ListeningEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "listeningId")
    val listeningId: Long,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "songId")
    val songId: Long,

    @ColumnInfo(name = "listenedAt")
    val listenedAt: Long
)