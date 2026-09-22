package com.example.vynl.data.local.database

import android.content.Context
import androidx.room.Room

object VynlDatabaseProvider {

    @Volatile
    private var INSTANCE: VynlDatabase? = null

    fun getDatabase(context: Context): VynlDatabase {

        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                VynlDatabase::class.java,
                "vynl_database"
            ).build()

            INSTANCE = instance

            instance
        }
    }
}