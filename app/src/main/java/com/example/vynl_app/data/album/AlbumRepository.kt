package com.example.vynl_app.data.album

// Contract for loading one album's full detail. Real implementation will call
// Last.fm's album.getInfo. UI only ever depends on this interface.
interface AlbumRepository {
    suspend fun getAlbumDetail(albumId: String): AlbumDetail
}
