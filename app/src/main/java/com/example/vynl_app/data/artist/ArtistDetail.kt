package com.example.vynl_app.data.artist

// Everything the Artist Detail screen shows about the artist itself. Releases live in
// ArtistRelease and tracks in ArtistTrack, so this stays a plain metadata record.
data class ArtistDetail(
    val id: String,
    val name: String,
    val tagline: String,
    val isVerified: Boolean,
    val monthlyListeners: Long,
    val followers: Long,
    val vynlIndex: Double,
    val biography: String
)
