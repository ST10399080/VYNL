package com.example.vynl_app.data.album

// Everything the Album Detail screen shows about the album itself. Ratings live in
// data/rating and reviews in data/review, so this stays a plain metadata record.
data class AlbumDetail(
    val id: String,
    val title: String,
    val artist: String,
    val artistId: String,
    val year: Int,
    val coverUrl: String,
    val tracks: List<Track>,
    // null means critics haven't scored it yet; the UI shows "Pending"
    val criticScore: Int?,
    // Decided by the data source (not a UI-side threshold) so the rule can change without touching the screen
    val isCommunityFavorite: Boolean
)
