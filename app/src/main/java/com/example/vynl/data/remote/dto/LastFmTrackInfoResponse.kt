package com.example.vynl.data.remote.dto

data class LastFmTrackInfoResponse(
    val track: LastFmTrackInfo
)

data class LastFmTrackInfo(
    val id: String?,
    val name: String,
    val mbid: String?,
    val url: String?,
    val duration: String?,
    val streamable: LastFmStreamable?,
    val listeners: String?,
    val playcount: String?,
    val artist: LastFmArtist,
    val album: LastFmTrackAlbum?,
    val toptags: LastFmTopTags?,
    val wiki: LastFmWiki?
)

data class LastFmTrackAlbum(
    val artist: String?,
    val title: String?,
    val mbid: String?,
    val url: String?,
    val image: List<LastFmImage>?
)