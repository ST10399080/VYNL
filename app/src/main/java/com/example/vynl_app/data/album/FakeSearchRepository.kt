package com.example.vynl_app.data.album

import kotlinx.coroutines.delay

// In-memory fake, no network at all. delay() mimics real API latency so
// the loading state you'll build in the UI actually gets tested now,
// instead of only being noticed once the real slow network call exists.
class FakeSearchRepository : SearchRepository {

    private val allAlbums = listOf(
        Album("1", "Bloom Atlas", "Night Static", "night-static", ""),
        Album("2", "Architectural Echoes", "Night Static", "night-static", ""),
        Album("3", "Cyberflora", "Synthwave Protocol", "synthwave-protocol", ""),
        Album("4", "Neon Drift", "Night Static", "night-static", "")
    )

    override suspend fun searchAlbums(query: String): List<Album> {
        delay(400) // fake network delay
        if (query.isBlank()) return emptyList()
        return allAlbums.filter {
            it.title.contains(query, ignoreCase = true) || it.artist.contains(query, ignoreCase = true)
        }
    }
}