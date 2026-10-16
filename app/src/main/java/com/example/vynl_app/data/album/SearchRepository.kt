package com.example.vynl_app.data.album

// Contract for album search. Real implementation (Monday) calls Last.fm's
// album.search endpoint. UI only ever depends on this interface.
interface SearchRepository {
    suspend fun searchAlbums(query: String): List<Album>
}