package com.example.vynl_app.data.artist

enum class ReleaseFormat {
    LP, EP, SINGLE
}

data class ArtistRelease(
    val id: String,
    val title: String,
    val format: ReleaseFormat,
    val year: Int,
    val trackCount: Int,
    val ratingPercentage: Int,
    val ratingCount: Long
)
