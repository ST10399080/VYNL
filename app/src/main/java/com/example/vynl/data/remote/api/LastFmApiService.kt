package com.example.vynl.data.remote.api

import com.example.vynl.data.remote.dto.LastFmAlbumInfoResponse
import com.example.vynl.data.remote.dto.LastFmAlbumSearchResponse
import com.example.vynl.data.remote.dto.LastFmTrackInfoResponse
import com.example.vynl.data.remote.dto.LastFmTrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {

    @GET("2.0/")
    suspend fun searchAlbums(
        @Query("method") method: String = "album.search",
        @Query("album") album: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30
    ): LastFmAlbumSearchResponse

    @GET("2.0/")
    suspend fun getAlbumInfo(
        @Query("method") method: String = "album.getInfo",
        @Query("artist") artist: String,
        @Query("album") album: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): LastFmAlbumInfoResponse

    @GET("2.0/")
    suspend fun searchTracks(
        @Query("method") method: String = "track.search",
        @Query("track") track: String,
        @Query("artist") artist: String? = null,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30
    ): LastFmTrackSearchResponse

    @GET("2.0/")
    suspend fun getTrackInfo(
        @Query("method") method: String = "track.getInfo",
        @Query("artist") artist: String,
        @Query("track") track: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): LastFmTrackInfoResponse
}