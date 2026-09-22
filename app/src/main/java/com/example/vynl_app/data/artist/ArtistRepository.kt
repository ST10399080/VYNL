package com.example.vynl_app.data.artist

// Contract for loading an artist's detail, essential releases, and popular tracks.
// Real implementation will call Last.fm's artist.getInfo / artist.getTopAlbums / artist.getTopTracks.
// UI only ever depends on this interface.
interface ArtistRepository {
    suspend fun getArtistDetail(artistId: String): ArtistDetail
    suspend fun getEssentialReleases(artistId: String): List<ArtistRelease>
    suspend fun getPopularTracks(artistId: String): List<ArtistTrack>
}
