package com.example.vynl.data.repository

import android.content.Context
import com.example.vynl.data.local.database.VynlDatabaseProvider

object VynlRepositoryProvider {

    @Volatile
    private var INSTANCE: VynlRepository? = null

    fun getRepository(context: Context): VynlRepository {

        return INSTANCE ?: synchronized(this) {

            val database = VynlDatabaseProvider.getDatabase(context)

            val repository = VynlRepositoryImpl(
                albumDao = database.albumDao(),
                songDao = database.songDao(),
                ratingDao = database.ratingDao(),
                reviewDao = database.reviewDao(),
                rankingDao = database.rankingDao(),
                listeningDao = database.listeningDao(),
                tierListItemDao = database.tierListItemDao()
            )

            INSTANCE = repository

            repository
        }
    }
}