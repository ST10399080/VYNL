package com.example.vynl_app.data.artist

import kotlinx.coroutines.delay

// In-memory fake matching the Artist Detail Figma frame (1:964). Returns the same artist
// for any id so the screen always has something to show while there's no backend.
class FakeArtistRepository : ArtistRepository {

    override suspend fun getArtistDetail(artistId: String): ArtistDetail {
        delay(300) // fake network delay so the loading state gets exercised
        return ArtistDetail(
            id = artistId,
            name = "Night Static",
            tagline = "Modular Synthesis · Berlin / Tokyo",
            isVerified = true,
            monthlyListeners = 1_800_000,
            followers = 142_000,
            vynlIndex = 92.4,
            biography = "Architect of brutalist electronics and analog modular spaces. Layering dense " +
                "sub-bass foundations with shattered glass treble textures, Night Static constructs " +
                "physical resonance into sonic architecture."
        )
    }

    override suspend fun getEssentialReleases(artistId: String): List<ArtistRelease> {
        delay(300)
        return listOf(
            ArtistRelease("1", "Bloom Atlas", ReleaseFormat.LP, 2026, 11, 88, 1_400),
            ArtistRelease("2", "Architectural Echoes", ReleaseFormat.EP, 2024, 5, 91, 820),
            ArtistRelease("3", "Static Void Phase", ReleaseFormat.SINGLE, 2023, 1, 94, 2_100)
        )
    }

    override suspend fun getPopularTracks(artistId: String): List<ArtistTrack> {
        delay(300)
        return listOf(
            ArtistTrack(1, "Hyperbloom", "Bloom Atlas · 3:42", 1_200_000),
            ArtistTrack(2, "Neon Drift", "Bloom Atlas · 4:15", 940_000),
            ArtistTrack(3, "Obsidian Frame", "Single · 4:12", 680_000)
        )
    }
}
