package com.example.vynl_app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeFeed

@Serializable
data object Search

@Serializable
data class AlbumDetail(val albumId: String)

@Serializable
data class ArtistDetail(val artistId: String)

@Serializable
data object Lists

@Serializable
data class ListDetail(val listId: String)

// mode is "ALBUMS" or "SONGS" — passed as a plain string, not the domain enum, so this
// route carries only primitives like every other one.
@Serializable
data class TierListBuilder(val artistId: String, val mode: String)

@Serializable
data class TierListViewer(val tierListId: String)
