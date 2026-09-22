package com.example.vynl_app.data.home

import kotlinx.coroutines.delay

// In-memory fake, no network at all. Reuses the 4 albums already established elsewhere in
// the app (Architectural Echoes, Cyberflora, Neon Drift, Static Void Phase) plus 4 new
// fictional entries in the same style, so the list has enough volume without repeating
// the hero pick.
class FakeHomeFeedRepository : HomeFeedRepository {

    override suspend fun getHeroAlbum(): FeaturedAlbum {
        delay(300) // fake network delay
        return FeaturedAlbum(albumId = "1", artistId = "night-static", title = "Bloom Atlas", artist = "Night Static")
    }

    override suspend fun getTrendingAlbums(): List<FeaturedAlbum> {
        delay(300)
        return listOf(
            FeaturedAlbum("2", "night-static", "Architectural Echoes", "Night Static"),
            FeaturedAlbum("3", "synthwave-protocol", "Cyberflora", "Synthwave Protocol"),
            FeaturedAlbum("4", "night-static", "Neon Drift", "Night Static"),
            FeaturedAlbum("5", "night-static", "Static Void Phase", "Night Static"),
            FeaturedAlbum("6", "vermilion-static", "Glass Horizon", "Vermilion Static"),
            FeaturedAlbum("7", "wavelength-ghost", "Halcyon Drift", "Wavelength Ghost"),
            FeaturedAlbum("8", "night-circuit", "Concrete Bloom", "Night Circuit"),
            FeaturedAlbum("9", "paper-moon-radio", "Afterimage", "Paper Moon Radio")
        )
    }
}
