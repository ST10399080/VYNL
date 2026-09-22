package com.example.vynl_app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Search

@Serializable
data class AlbumDetail(val albumId: String)

@Serializable
data class ArtistDetail(val artistId: String)
