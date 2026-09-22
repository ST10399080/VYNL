package com.example.vynl_app.data.album

import kotlinx.coroutines.delay

// In-memory fake matching the Album Detail Figma frame. Returns the same album
// for any id so the screen always has something to show while there's no backend.
class FakeAlbumRepository : AlbumRepository {

    override suspend fun getAlbumDetail(albumId: String): AlbumDetail {
        delay(300) // fake network delay so the loading state gets exercised
        return AlbumDetail(
            id = albumId,
            title = "Bloom Atlas",
            artist = "Night Static",
            artistId = "night-static",
            year = 2026,
            coverUrl = "",
            tracks = listOf(
                Track(1, "Hyperbloom", 222),
                Track(2, "Neon Drift", 255),
                Track(3, "Static Architecture", 301),
                Track(4, "Void Protocols", 178)
            ),
            criticScore = null,
            isCommunityFavorite = true
        )
    }
}
