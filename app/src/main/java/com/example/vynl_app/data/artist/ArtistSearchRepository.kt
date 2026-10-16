package com.example.vynl_app.data.artist

// Contract for artist search. Real implementation calls Last.fm's artist.search endpoint.
// UI only ever depends on this interface.
interface ArtistSearchRepository {
    suspend fun searchArtists(query: String): List<ArtistSummary>
}
