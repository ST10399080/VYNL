package com.example.vynl_app.data.home

// Contract for the Home Feed's hero pick and "Top" list. Deliberately separate from
// SearchRepository — this is "here's today's featured content," not a search abstraction.
// Real implementation will call a trending/recommendation endpoint.
interface HomeFeedRepository {
    suspend fun getHeroAlbum(): FeaturedAlbum
    suspend fun getTrendingAlbums(): List<FeaturedAlbum>
}
