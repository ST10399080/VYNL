package com.example.vynl_app.data.artist

// subtitle holds the pre-formatted "Album · 3:42" / "Single · 4:12" line as drawn in the
// Figma frame, rather than decomposing into separate album/duration fields.
data class ArtistTrack(
    val rank: Int,
    val title: String,
    val subtitle: String,
    val playCount: Long
)
