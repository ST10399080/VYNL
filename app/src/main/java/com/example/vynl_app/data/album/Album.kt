package com.example.vynl_app.data.album

// Minimal shape needed for search results and Album Detail for now.
// Real fields (track list, release year, etc.) get added once Last.fm is wired in Monday.
data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val artistId: String,
    val coverUrl: String
)