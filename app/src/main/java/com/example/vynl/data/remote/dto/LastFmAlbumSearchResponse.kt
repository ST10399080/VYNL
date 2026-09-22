package com.example.vynl.data.remote.dto

data class LastFmAlbumSearchResponse(
    val results: AlbumSearchResults
)

data class AlbumSearchResults(
    val albummatches: AlbumMatches
)

data class AlbumMatches(
    val album: List<LastFmAlbumSearchItem>
)

data class LastFmAlbumSearchItem(
    val name: String,
    val artist: String,
    val mbid: String?,
    val url: String?,
    val image: List<LastFmImage>?,
    val streamable: String?
)