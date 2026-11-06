package com.example.vynl

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vynl.data.remote.api.LastFmApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import com.example.vynl.data.remote.mapper.toAlbumEntity
import com.example.vynl.data.remote.mapper.toSongEntity

@RunWith(AndroidJUnit4::class)
class LastFmApiTest {

    @Test
    fun testAlbumSearch() {
        runBlocking {

            val apiKey = "5657871f7b3c2e1e201bef2c4c12c44b"

            val response = LastFmApiClient.api.searchAlbums(
                album = "Believe",
                apiKey = apiKey
            )

            println("===== LAST.FM TEST =====")

            println(
                "Number of albums: " +
                        response.results.albummatches.album.size
            )

            response.results.albummatches.album.forEach { album ->

                println("Album: ${album.name}")
                println("Artist: ${album.artist}")
                println("MBID: ${album.mbid}")
                println("Images: ${album.image}")
                println("-----------------------")
            }
        }
    }

    @Test
    fun testTrackMapping() {
        runBlocking {

            val apiKey = "5657871f7b3c2e1e201bef2c4c12c44b"

            // 1. Get a REAL album response from Last.fm
            val response = LastFmApiClient.api.getAlbumInfo(
                artist = "Cher",
                album = "Believe",
                apiKey = apiKey
            )

            // 2. Get the album DTO
            val albumDto = response.album

            // 3. Convert the album DTO to an AlbumEntity
            val albumEntity = albumDto.toAlbumEntity()

            // 4. Get the real tracks from the Last.fm response
            val tracks = albumDto.tracks?.track.orEmpty()

            println("===== TRACK MAPPER TEST =====")

            println("Number of tracks: ${tracks.size}")
            println()

            // 5. Map every Last.fm track to a SongEntity
            tracks.forEach { trackDto ->

                val songEntity = trackDto.toSongEntity(
                    albumId = albumEntity.albumId
                )

                println("----- TRACK -----")

                println("Last.fm DTO:")
                println("Name: ${trackDto.name}")
                println("MBID: ${trackDto.mbid}")
                println("Artist: ${trackDto.artist.name}")
                println("Duration: ${trackDto.duration}")
                println("Rank: ${trackDto.attr?.rank}")
                println("Streamable: ${trackDto.streamable?.text}")

                println()

                println("SongEntity:")

                if (songEntity != null) {
                    println("Track ID: ${songEntity.lastFmTrackId}")
                    println("Title: ${songEntity.title}")
                    println("Artist: ${songEntity.artistName}")
                    println("Duration: ${songEntity.duration}")
                    println("Track number: ${songEntity.trackNumber}")
                    println("Image URL: ${songEntity.imageUrl}")
                    println("Streamable: ${songEntity.streamable}")
                    println("Album ID: ${songEntity.albumId}")
                } else {
                    println("SongEntity: NULL")
                }

                println()
            }
        }
    }

    @Test
    fun testAlbumMapping() {
        runBlocking {

            val apiKey = "5657871f7b3c2e1e201bef2c4c12c44b"

            // 1. Get a REAL album from Last.fm
            val response = LastFmApiClient.api.getAlbumInfo(
                artist = "Cher",
                album = "Believe",
                apiKey = apiKey
            )

            // 2. Get the DTO from the response
            val albumDto = response.album

            // 3. Map the DTO to our Room entity
            val albumEntity = albumDto.toAlbumEntity()

            println("===== ALBUM MAPPER TEST =====")

            println("----- LAST.FM DTO -----")
            println("Name: ${albumDto.name}")
            println("Artist: ${albumDto.artist}")
            println("MBID: ${albumDto.mbid}")
            println("Release date: ${albumDto.releasedate}")
            println("Image: ${albumDto.image}")
            println("Genre: ${albumDto.toptags?.tag?.firstOrNull()?.name}")
            println("Description: ${albumDto.wiki?.summary}")
            println("Track count: ${albumDto.tracks?.track?.size}")

            println()
            println("----- ALBUM ENTITY -----")
            println("Album ID: ${albumEntity.albumId}")
            println("Last.fm MBID: ${albumEntity.lastFmMbid}")
            println("Album name: ${albumEntity.albumName}")
            println("Artist name: ${albumEntity.artistName}")
            println("Artist MBID: ${albumEntity.artistMbid}")
            println("Release date: ${albumEntity.releaseDate}")
            println("Genre: ${albumEntity.genre}")
            println("Image URL: ${albumEntity.imageUrl}")
            println("Description: ${albumEntity.description}")
            println("Track count: ${albumEntity.trackCount}")
            println("Created at: ${albumEntity.createdAt}")
            println("Updated at: ${albumEntity.updatedAt}")
        }
    }

    @Test
    fun testTrackSearch() {
        runBlocking {

            val apiKey = "5657871f7b3c2e1e201bef2c4c12c44b"

            val response = LastFmApiClient.api.searchTracks(
                track = "Believe",
                artist = "Cher",
                apiKey = apiKey
            )

            println("===== TRACK SEARCH TEST =====")

            val tracks = response.results.trackmatches.track

            println("Number of tracks: ${tracks.size}")

            tracks.forEach { track ->

                println("Track: ${track.name}")
                println("Artist: ${track.artist}")
                println("URL: ${track.url}")
                println("Streamable: ${track.streamable}")
                println("Listeners: ${track.listeners}")
                println("Images: ${track.image}")

                println("-----------------------")
            }
        }
    }

    @Test
    fun testTrackInfo() {
        runBlocking {

            val apiKey = "5657871f7b3c2e1e201bef2c4c12c44b"

            val response = LastFmApiClient.api.getTrackInfo(
                artist = "Cher",
                track = "Believe",
                apiKey = apiKey
            )

            val track = response.track

            println("===== TRACK INFO TEST =====")

            println("ID: ${track.id}")
            println("Name: ${track.name}")
            println("MBID: ${track.mbid}")
            println("URL: ${track.url}")
            println("Duration: ${track.duration}")
            println("Listeners: ${track.listeners}")
            println("Play count: ${track.playcount}")
            println("Artist: ${track.artist.name}")

            println("Album:")
            println("Title: ${track.album?.title}")
            println("MBID: ${track.album?.mbid}")

            println("Tags:")
            track.toptags?.tag?.forEach { tag ->
                println(tag.name)
            }

            println("Wiki:")
            println(track.wiki?.summary)
        }
    }

    @Test
    fun testAlbumInfo() {
        runBlocking {

            val apiKey = "5657871f7b3c2e1e201bef2c4c12c44b"

            val response = LastFmApiClient.api.getAlbumInfo(
                artist = "Cher",
                album = "Believe",
                apiKey = apiKey
            )

            val album = response.album

            println("===== ALBUM INFO TEST =====")

            println("Name: ${album.name}")
            println("Artist: ${album.artist}")
            println("MBID: ${album.mbid}")
            println("Release date: ${album.releasedate}")
            println("Genre: ${album.toptags?.tag?.firstOrNull()?.name}")
            println("Description: ${album.wiki?.summary}")

            println("Tracks:")

            album.tracks?.track?.forEach { track ->

                println(
                    "${track.attr?.rank}. " +
                            "${track.name} - " +
                            "${track.duration}s"
                )
            }
        }
    }

    @Test
    fun testTrackInfoMapping() {
        runBlocking {

            val apiKey = "5657871f7b3c2e1e201bef2c4c12c44b"

            // 1. Get a REAL track from Last.fm
            val response = LastFmApiClient.api.getTrackInfo(
                artist = "Cher",
                track = "Believe",
                apiKey = apiKey
            )

            // 2. Get the track DTO
            val trackDto = response.track

            // 3. Map the DTO to SongEntity
            val songEntity = trackDto.toSongEntity(
                albumId = 0L
            )

            println("===== TRACK INFO MAPPER TEST =====")

            println("----- LAST.FM DTO -----")
            println("Name: ${trackDto.name}")
            println("MBID: ${trackDto.mbid}")
            println("Artist: ${trackDto.artist.name}")
            println("Duration: ${trackDto.duration}")
            println("Streamable: ${trackDto.streamable?.text}")
            println("Album: ${trackDto.album?.title}")
            println("Album MBID: ${trackDto.album?.mbid}")

            println()

            println("----- SONG ENTITY -----")

            if (songEntity != null) {
                println("Song ID: ${songEntity.songId}")
                println("Last.fm Track ID: ${songEntity.lastFmTrackId}")
                println("Album ID: ${songEntity.albumId}")
                println("Title: ${songEntity.title}")
                println("Artist: ${songEntity.artistName}")
                println("Duration: ${songEntity.duration}")
                println("Track Number: ${songEntity.trackNumber}")
                println("Image URL: ${songEntity.imageUrl}")
                println("Streamable: ${songEntity.streamable}")
                println("Created At: ${songEntity.createdAt}")
            } else {
                println("SongEntity: NULL")
            }
        }
    }
}