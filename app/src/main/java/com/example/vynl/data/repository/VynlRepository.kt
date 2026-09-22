package com.example.vynl.data.repository

import com.example.vynl.data.local.entity.AlbumEntity
import com.example.vynl.data.local.entity.ListeningEntity
import com.example.vynl.data.local.entity.RankingEntity
import com.example.vynl.data.local.entity.RatingEntity
import com.example.vynl.data.local.entity.ReviewEntity
import com.example.vynl.data.local.entity.SongEntity
import com.example.vynl.data.local.entity.TierListItemEntity
import kotlinx.coroutines.flow.Flow

interface VynlRepository {

    // Albums
    suspend fun insertAlbum(album: AlbumEntity)
    suspend fun insertAlbums(albums: List<AlbumEntity>)
    suspend fun updateAlbum(album: AlbumEntity)
    suspend fun deleteAlbum(album: AlbumEntity)
    fun getAllAlbums(): Flow<List<AlbumEntity>>
    suspend fun getAlbumById(albumId: Long): AlbumEntity?
    fun getAlbumsByArtist(artistName: String): Flow<List<AlbumEntity>>
    suspend fun deleteAllAlbums()

    // Last.fm
    suspend fun searchAlbumsFromLastFm(
        album: String
    ): List<AlbumEntity>

    suspend fun getAlbumFromLastFm(
        artist: String,
        album: String
    ): AlbumEntity?

    suspend fun getSongsFromLastFmAlbum(
        artist: String,
        album: String
    ): List<SongEntity>

    suspend fun importAlbumFromLastFm(
        artist: String,
        album: String
    ): Long

    // Songs
    suspend fun insertSong(song: SongEntity)
    suspend fun insertSongs(songs: List<SongEntity>)
    suspend fun updateSong(song: SongEntity)
    suspend fun deleteSong(song: SongEntity)
    fun getAllSongs(): Flow<List<SongEntity>>
    suspend fun getSongById(songId: Long): SongEntity?
    fun getSongsByAlbum(albumId: Long): Flow<List<SongEntity>>
    suspend fun getSongByLastFmTrackId(lastFmTrackId: String): SongEntity?
    suspend fun deleteSongsByAlbum(albumId: Long)
    suspend fun deleteAllSongs()

    // Ratings
    suspend fun insertRating(rating: RatingEntity)
    suspend fun updateRating(rating: RatingEntity)
    suspend fun deleteRating(rating: RatingEntity)
    suspend fun getRating(userId: String, albumId: Long): RatingEntity?
    fun getUserRatings(userId: String): Flow<List<RatingEntity>>
    fun getAlbumRatings(albumId: Long): Flow<List<RatingEntity>>
    fun observeRating(
        userId: String,
        albumId: Long
    ): Flow<RatingEntity?>
    suspend fun deleteUserRatings(userId: String)

    // Reviews
    suspend fun insertReview(review: ReviewEntity)
    suspend fun updateReview(review: ReviewEntity)
    suspend fun deleteReview(review: ReviewEntity)
    suspend fun getReview(
        userId: String,
        albumId: Long
    ): ReviewEntity?
    fun getUserReviews(userId: String): Flow<List<ReviewEntity>>
    fun getAlbumReviews(albumId: Long): Flow<List<ReviewEntity>>
    fun observeReview(
        userId: String,
        albumId: Long
    ): Flow<ReviewEntity?>
    suspend fun deleteUserReviews(userId: String)

    // Rankings
    suspend fun insertRanking(ranking: RankingEntity)
    suspend fun updateRanking(ranking: RankingEntity)
    suspend fun deleteRanking(ranking: RankingEntity)
    fun getUserRankings(userId: String): Flow<List<RankingEntity>>
    suspend fun getRanking(
        userId: String,
        albumId: Long
    ): RankingEntity?
    suspend fun getRankingAtPosition(
        userId: String,
        rankPosition: Int
    ): RankingEntity?
    suspend fun deleteUserRankings(userId: String)

    // Listening history
    suspend fun insertListening(listening: ListeningEntity)
    suspend fun insertListeningItems(items: List<ListeningEntity>)
    suspend fun deleteListening(listening: ListeningEntity)
    fun getUserListeningHistory(
        userId: String
    ): Flow<List<ListeningEntity>>
    fun getSongListeningHistory(
        userId: String,
        songId: Long
    ): Flow<List<ListeningEntity>>
    fun getRecentListening(
        userId: String,
        limit: Int
    ): Flow<List<ListeningEntity>>
    suspend fun deleteUserListeningHistory(userId: String)

    // Tier list
    suspend fun insertTierListItem(item: TierListItemEntity)
    suspend fun insertTierListItems(
        items: List<TierListItemEntity>
    )
    suspend fun updateTierListItem(item: TierListItemEntity)
    suspend fun deleteTierListItem(item: TierListItemEntity)
    fun getUserTierListItems(
        userId: String
    ): Flow<List<TierListItemEntity>>
    fun getItemsByTier(
        userId: String,
        tier: String
    ): Flow<List<TierListItemEntity>>
    fun getItemsByType(
        userId: String,
        itemType: String
    ): Flow<List<TierListItemEntity>>
    suspend fun deleteUserTierListItems(userId: String)
}