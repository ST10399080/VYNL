package com.example.vynl_app.data.artist

import kotlinx.coroutines.delay

// In-memory fake, no network at all. delay() mimics real API latency so
// the loading state gets exercised now, not only once the real network call exists.
class FakeArtistSearchRepository : ArtistSearchRepository {

    private val allArtists = listOf(
        ArtistSummary("night-static", "Night Static", "Modular Synthesis · Berlin / Tokyo"),
        ArtistSummary("synthwave-protocol", "Synthwave Protocol", "Synthwave · Los Angeles")
    )

    override suspend fun searchArtists(query: String): List<ArtistSummary> {
        delay(400) // fake network delay
        if (query.isBlank()) return emptyList()
        return allArtists.filter { it.name.contains(query, ignoreCase = true) }
    }
}
