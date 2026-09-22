package com.example.vynl

import androidx.test.platform.app.InstrumentationRegistry
import com.example.vynl.data.repository.VynlRepositoryProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class VynlLastFmImportTest {

    @Test
    fun testImportAlbumFromLastFm() {
        runBlocking {

            val context =
                InstrumentationRegistry.getInstrumentation().targetContext

            val repository =
                VynlRepositoryProvider.getRepository(context)

            println("===== VYNL LAST.FM IMPORT TEST =====")
            println()

            // --------------------------------------------------
            // 1. Import album from Last.fm
            // --------------------------------------------------

            val albumId = repository.importAlbumFromLastFm(
                artist = "Cher",
                album = "Believe"
            )

            println("Generated Album ID: $albumId")
            println()

            // --------------------------------------------------
            // 2. Read album back from Room
            // --------------------------------------------------

            val album = repository.getAlbumById(albumId)

            println("----- ALBUM FROM ROOM -----")

            if (album != null) {
                println("Album ID: ${album.albumId}")
                println("Album Name: ${album.albumName}")
                println("Artist: ${album.artistName}")
                println("MBID: ${album.lastFmMbid}")
                println("Release Date: ${album.releaseDate}")
                println("Genre: ${album.genre}")
                println("Image URL: ${album.imageUrl}")
                println("Track Count: ${album.trackCount}")
            } else {
                println("Album was NOT found in Room.")
            }

            println()

            // --------------------------------------------------
            // 3. Read songs back from Room
            // --------------------------------------------------

            val songs = repository
                .getSongsByAlbum(albumId)
                .first()

            println("----- SONGS FROM ROOM -----")

            println("Number of songs: ${songs.size}")
            println()

            songs.forEach { song ->

                println("Song ID: ${song.songId}")
                println("Last.fm Track ID: ${song.lastFmTrackId}")
                println("Album ID: ${song.albumId}")
                println("Title: ${song.title}")
                println("Artist: ${song.artistName}")
                println("Duration: ${song.duration}")
                println("Track Number: ${song.trackNumber}")
                println("Streamable: ${song.streamable}")
                println()
            }
        }
    }
}