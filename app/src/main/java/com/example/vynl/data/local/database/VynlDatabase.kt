package com.example.vynl.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vynl.data.local.dao.AlbumDao
import com.example.vynl.data.local.dao.ListeningDao
import com.example.vynl.data.local.dao.RankingDao
import com.example.vynl.data.local.dao.RatingDao
import com.example.vynl.data.local.dao.ReviewDao
import com.example.vynl.data.local.dao.SongDao
import com.example.vynl.data.local.dao.TierListItemDao
import com.example.vynl.data.local.entity.AlbumEntity
import com.example.vynl.data.local.entity.ListeningEntity
import com.example.vynl.data.local.entity.RankingEntity
import com.example.vynl.data.local.entity.RatingEntity
import com.example.vynl.data.local.entity.ReviewEntity
import com.example.vynl.data.local.entity.SongEntity
import com.example.vynl.data.local.entity.TierListItemEntity

@Database(
    entities = [
        AlbumEntity::class,
        SongEntity::class,
        RatingEntity::class,
        ReviewEntity::class,
        RankingEntity::class,
        ListeningEntity::class,
        TierListItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VynlDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao

    abstract fun songDao(): SongDao

    abstract fun ratingDao(): RatingDao

    abstract fun reviewDao(): ReviewDao

    abstract fun rankingDao(): RankingDao

    abstract fun listeningDao(): ListeningDao

    abstract fun tierListItemDao(): TierListItemDao
}