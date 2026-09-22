package com.example.vynl_app.data.song

import kotlinx.coroutines.delay

class FakeSongSearchRepository : SongSearchRepository {

    private val allSongs = listOf(
        Song("1", "Hyperbloom", "Night Static", "Bloom Atlas"),
        Song("2", "Neon Drift", "Night Static", "Bloom Atlas"),
        Song("3", "Static Architecture", "Night Static", "Bloom Atlas"),
        Song("4", "Void Protocols", "Night Static", "Bloom Atlas")
    )

    override suspend fun searchSongs(query: String): List<Song> {
        delay(400)
        if (query.isBlank()) return emptyList()
        return allSongs.filter {
            it.title.contains(query, ignoreCase = true) || it.artist.contains(query, ignoreCase = true)
        }
    }
}