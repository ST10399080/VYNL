package com.example.vynl.data.remote.dto

data class LastFmTrackSearchResponse(
    val results: TrackSearchResults
)

data class TrackSearchResults(
    val trackmatches: TrackMatches
)

data class TrackMatches(
    val track: List<LastFmTrackSearchItem>
)

data class LastFmTrackSearchItem(
    val name: String,
    val artist: String,
    val url: String?,
    val streamable: String?,
    val listeners: String?,
    val image: List<LastFmImage>?
)