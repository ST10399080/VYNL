package com.example.vynl_app.data.artist

// Minimal shape needed for artist search results. Full detail lives in ArtistDetail.
data class ArtistSummary(
    val id: String,
    val name: String,
    val tagline: String
)
