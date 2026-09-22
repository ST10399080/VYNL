package com.example.vynl_app.data.home

// Minimal shape for a Home Feed row (hero or list). Carries both albumId and artistId so a
// row can link to Album Detail and its artist name can separately link to Artist Detail.
data class FeaturedAlbum(
    val albumId: String,
    val artistId: String,
    val title: String,
    val artist: String
)
