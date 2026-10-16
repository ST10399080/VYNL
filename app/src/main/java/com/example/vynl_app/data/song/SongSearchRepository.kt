package com.example.vynl_app.data.song

interface SongSearchRepository {
    suspend fun searchSongs(query: String): List<Song>
}