package com.example.vynl.data.remote.mapper

import com.example.vynl.data.local.entity.AlbumEntity
import com.example.vynl.data.local.entity.SongEntity
import com.example.vynl.data.remote.dto.LastFmAlbumInfo
import com.example.vynl.data.remote.dto.LastFmAlbumSearchItem
import com.example.vynl.data.remote.dto.LastFmTrack
import com.example.vynl.data.remote.dto.LastFmTrackInfo

private fun List<com.example.vynl.data.remote.dto.LastFmImage>?.getLargeImageUrl(): String? {
    return this
        ?.firstOrNull { it.size == "extralarge" }
        ?.text
        ?: this?.firstOrNull { it.size == "large" }?.text
        ?: this?.firstOrNull { it.size == "medium" }?.text
        ?: this?.firstOrNull()?.text
}

fun LastFmAlbumSearchItem.toAlbumEntity(): AlbumEntity {
    val now = System.currentTimeMillis()

    return AlbumEntity(
        lastFmMbid = mbid?.takeIf { it.isNotBlank() },
        albumName = name,
        artistName = artist,
        artistMbid = null,
        releaseDate = null,
        genre = null,
        imageUrl = image.getLargeImageUrl(),
        description = null,
        trackCount = 0,
        createdAt = now,
        updatedAt = now
    )
}

fun LastFmAlbumInfo.toAlbumEntity(): AlbumEntity {
    val now = System.currentTimeMillis()

    val genre = toptags
        ?.tag
        ?.firstOrNull()
        ?.name

    return AlbumEntity(
        lastFmMbid = mbid?.takeIf { it.isNotBlank() },
        albumName = name,
        artistName = artist,
        artistMbid = null,
        releaseDate = releasedate?.takeIf { it.isNotBlank() },
        genre = genre,
        imageUrl = image.getLargeImageUrl(),
        description = wiki?.summary?.takeIf { it.isNotBlank() },
        trackCount = tracks?.track?.size ?: 0,
        createdAt = now,
        updatedAt = now
    )
}

fun LastFmTrack.toSongEntity(
    albumId: Long
): SongEntity? {

    val trackId = mbid?.takeIf { it.isNotBlank() }
        ?: url?.takeIf { it.isNotBlank() }
        ?: return null

    val durationSeconds = duration
        ?.toIntOrNull()

    val trackNumber = attr
        ?.rank
        ?.toIntOrNull()

    return SongEntity(
        lastFmTrackId = trackId,
        albumId = albumId,
        title = name,
        artistName = artist.name,
        duration = durationSeconds,
        trackNumber = trackNumber,
        imageUrl = null,
        streamable = streamable?.text == "1"
    )
}

fun LastFmTrackInfo.toSongEntity(
    albumId: Long
): SongEntity? {

    val trackId = mbid?.takeIf { it.isNotBlank() }
        ?: url?.takeIf { it.isNotBlank() }
        ?: return null

    val durationMilliseconds = duration?.toLongOrNull()

    val durationSeconds = durationMilliseconds
        ?.div(1000)
        ?.toInt()

    return SongEntity(
        lastFmTrackId = trackId,
        albumId = albumId,
        title = name,
        artistName = artist.name,
        duration = durationSeconds,
        trackNumber = null,
        imageUrl = album?.image.getLargeImageUrl(),
        streamable = streamable?.text == "1"
    )
}

