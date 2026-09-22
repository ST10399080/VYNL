package com.example.vynl.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LastFmAlbumInfoResponse(
    val album: LastFmAlbumInfo
)

data class LastFmAlbumInfo(
    val name: String,
    val artist: String,
    val mbid: String?,
    val url: String?,
    val releasedate: String?,
    val image: List<LastFmImage>?,
    val listeners: String?,
    val playcount: String?,
    val toptags: LastFmTopTags?,
    val tracks: LastFmTracks?,
    val wiki: LastFmWiki?
)

data class LastFmTracks(
    val track: List<LastFmTrack>
)

data class LastFmTrack(
    val name: String,
    val duration: String?,
    val mbid: String?,
    val url: String?,
    val streamable: LastFmStreamable?,
    val artist: LastFmArtist,

    @SerializedName("@attr")
    val attr: LastFmTrackAttributes?
)

data class LastFmTrackAttributes(
    val rank: String?
)

data class LastFmArtist(
    val name: String,
    val mbid: String?,
    val url: String?
)

data class LastFmStreamable(
    val fulltrack: String?,
    val text: String?
)

data class LastFmTopTags(
    val tag: List<LastFmTag>?
)

data class LastFmTag(
    val name: String,
    val url: String?
)

data class LastFmWiki(
    val published: String?,
    val summary: String?,
    val content: String?
)