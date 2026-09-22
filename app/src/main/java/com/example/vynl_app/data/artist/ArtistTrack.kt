package com.example.vynl_app.data.artist

// subtitle holds the pre-formatted "Album · 3:42" / "Single · 4:12" line as drawn in the
// Figma frame, rather than decomposing into separate album/duration fields.
data class ArtistTrack(
    val id: String,
    val rank: Int,
    val title: String,
    val subtitle: String,
    val playCount: Long,
    // Null for a standalone single with no parent album (see subtitle "Single · ...").
    // The row only navigates to Album Detail when this is non-null.
    val albumId: String?
)
