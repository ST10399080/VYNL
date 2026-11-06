package com.example.vynl.data.repository

import com.example.vynl.BuildConfig
import com.example.vynl.data.local.dao.AlbumDao
import com.example.vynl.data.local.dao.ListeningDao
import com.example.vynl.data.local.dao.RankingDao
import com.example.vynl.data.local.dao.RatingDao
import com.example.vynl.data.local.dao.ReviewDao
import com.example.vynl.data.local.dao.SongDao
import com.example.vynl.data.local.dao.TierListItemDao
import com.example.vynl.data.local.entity.AlbumEntity
import com.example.vynl.data.local.entity.ListeningEntity
import com.example.vynl.data.local.entity.RankingEntity
import com.example.vynl.data.local.entity.RatingEntity
import com.example.vynl.data.local.entity.ReviewEntity
import com.example.vynl.data.local.entity.SongEntity
import com.example.vynl.data.local.entity.TierListItemEntity
import kotlinx.coroutines.flow.Flow
import com.example.vynl.data.remote.api.LastFmApiClient
import com.example.vynl.data.remote.mapper.toAlbumEntity
import com.example.vynl.data.remote.mapper.toSongEntity

class VynlRepositoryImpl(
    private val albumDao: AlbumDao,
    private val songDao: SongDao,
    private val ratingDao: RatingDao,
    private val reviewDao: ReviewDao,
    private val rankingDao: RankingDao,
    private val listeningDao: ListeningDao,
    private val tierListItemDao: TierListItemDao
) : VynlRepository {

    private val lastFmApiKey = BuildConfig.LASTFM_API_KEY

    // --------------------------------------------------
    // Albums
    // --------------------------------------------------

    override suspend fun insertAlbum(album: AlbumEntity) {
        albumDao.insertAlbum(album)
    }

    override suspend fun insertAlbums(albums: List<AlbumEntity>) {
        albumDao.insertAlbums(albums)
    }

    override suspend fun updateAlbum(album: AlbumEntity) {
        albumDao.updateAlbum(album)
    }

    override suspend fun deleteAlbum(album: AlbumEntity) {
        albumDao.deleteAlbum(album)
    }

    override fun getAllAlbums(): Flow<List<AlbumEntity>> {
        return albumDao.getAllAlbums()
    }

    override suspend fun getAlbumById(albumId: Long): AlbumEntity? {
        return albumDao.getAlbumById(albumId)
    }

    override fun getAlbumsByArtist(
        artistName: String
    ): Flow<List<AlbumEntity>> {
        return albumDao.getAlbumsByArtist(artistName)
    }

    override suspend fun deleteAllAlbums() {
        albumDao.deleteAllAlbums()
    }

    // --------------------------------------------------
// Last.fm
// --------------------------------------------------

    override suspend fun searchAlbumsFromLastFm(
        album: String
    ): List<AlbumEntity> {

        val response = LastFmApiClient.api.searchAlbums(
            album = album,
            apiKey = lastFmApiKey
        )

        return response.results
            .albummatches
            .album
            .map { it.toAlbumEntity() }
    }

    override suspend fun getAlbumFromLastFm(
        artist: String,
        album: String
    ): AlbumEntity? {

        val response = LastFmApiClient.api.getAlbumInfo(
            artist = artist,
            album = album,
            apiKey = lastFmApiKey
        )

        return response.album.toAlbumEntity()
    }

    override suspend fun getSongsFromLastFmAlbum(
        artist: String,
        album: String
    ): List<SongEntity> {

        val response = LastFmApiClient.api.getAlbumInfo(
            artist = artist,
            album = album,
            apiKey = lastFmApiKey
        )

        val albumDto = response.album

        val albumEntity = albumDto.toAlbumEntity()

        return albumDto.tracks
            ?.track
            .orEmpty()
            .mapNotNull { track ->
                track.toSongEntity(
                    albumId = albumEntity.albumId
                )
            }
    }

    override suspend fun importAlbumFromLastFm(
        artist: String,
        album: String
    ): Long {

        val response = LastFmApiClient.api.getAlbumInfo(
            artist = artist,
            album = album,
            apiKey = lastFmApiKey
        )

        val albumDto = response.album

        val albumEntity = albumDto.toAlbumEntity()

        val albumId = albumDao.insertAlbum(albumEntity)

        val songs = albumDto.tracks
            ?.track
            .orEmpty()
            .mapNotNull { track ->
                track.toSongEntity(
                    albumId = albumId
                )
            }

        if (songs.isNotEmpty()) {
            songDao.insertSongs(songs)
        }

        return albumId
    }

    // --------------------------------------------------
    // Songs
    // --------------------------------------------------

    override suspend fun insertSong(song: SongEntity) {
        songDao.insertSong(song)
    }

    override suspend fun insertSongs(songs: List<SongEntity>) {
        songDao.insertSongs(songs)
    }

    override suspend fun updateSong(song: SongEntity) {
        songDao.updateSong(song)
    }

    override suspend fun deleteSong(song: SongEntity) {
        songDao.deleteSong(song)
    }

    override fun getAllSongs(): Flow<List<SongEntity>> {
        return songDao.getAllSongs()
    }

    override suspend fun getSongById(songId: Long): SongEntity? {
        return songDao.getSongById(songId)
    }

    override fun getSongsByAlbum(
        albumId: Long
    ): Flow<List<SongEntity>> {
        return songDao.getSongsByAlbum(albumId)
    }

    override suspend fun getSongByLastFmTrackId(
        lastFmTrackId: String
    ): SongEntity? {
        return songDao.getSongByLastFmTrackId(lastFmTrackId)
    }

    override suspend fun deleteSongsByAlbum(albumId: Long) {
        songDao.deleteSongsByAlbum(albumId)
    }

    override suspend fun deleteAllSongs() {
        songDao.deleteAllSongs()
    }

    // --------------------------------------------------
    // Ratings
    // --------------------------------------------------

    override suspend fun insertRating(rating: RatingEntity) {
        ratingDao.insertRating(rating)
    }

    override suspend fun updateRating(rating: RatingEntity) {
        ratingDao.updateRating(rating)
    }

    override suspend fun deleteRating(rating: RatingEntity) {
        ratingDao.deleteRating(rating)
    }

    override suspend fun getRating(
        userId: String,
        albumId: Long
    ): RatingEntity? {
        return ratingDao.getRating(userId, albumId)
    }

    override fun getUserRatings(
        userId: String
    ): Flow<List<RatingEntity>> {
        return ratingDao.getUserRatings(userId)
    }

    override fun getAlbumRatings(
        albumId: Long
    ): Flow<List<RatingEntity>> {
        return ratingDao.getAlbumRatings(albumId)
    }

    override fun observeRating(
        userId: String,
        albumId: Long
    ): Flow<RatingEntity?> {
        return ratingDao.observeRating(userId, albumId)
    }

    override suspend fun deleteUserRatings(userId: String) {
        ratingDao.deleteUserRatings(userId)
    }

    // --------------------------------------------------
    // Reviews
    // --------------------------------------------------

    override suspend fun insertReview(review: ReviewEntity) {
        reviewDao.insertReview(review)
    }

    override suspend fun updateReview(review: ReviewEntity) {
        reviewDao.updateReview(review)
    }

    override suspend fun deleteReview(review: ReviewEntity) {
        reviewDao.deleteReview(review)
    }

    override suspend fun getReview(
        userId: String,
        albumId: Long
    ): ReviewEntity? {
        return reviewDao.getReview(userId, albumId)
    }

    override fun getUserReviews(
        userId: String
    ): Flow<List<ReviewEntity>> {
        return reviewDao.getUserReviews(userId)
    }

    override fun getAlbumReviews(
        albumId: Long
    ): Flow<List<ReviewEntity>> {
        return reviewDao.getAlbumReviews(albumId)
    }

    override fun observeReview(
        userId: String,
        albumId: Long
    ): Flow<ReviewEntity?> {
        return reviewDao.observeReview(userId, albumId)
    }

    override suspend fun deleteUserReviews(userId: String) {
        reviewDao.deleteUserReviews(userId)
    }

    // --------------------------------------------------
    // Rankings
    // --------------------------------------------------

    override suspend fun insertRanking(ranking: RankingEntity) {
        rankingDao.insertRanking(ranking)
    }

    override suspend fun updateRanking(ranking: RankingEntity) {
        rankingDao.updateRanking(ranking)
    }

    override suspend fun deleteRanking(ranking: RankingEntity) {
        rankingDao.deleteRanking(ranking)
    }

    override fun getUserRankings(
        userId: String
    ): Flow<List<RankingEntity>> {
        return rankingDao.getUserRankings(userId)
    }

    override suspend fun getRanking(
        userId: String,
        albumId: Long
    ): RankingEntity? {
        return rankingDao.getRanking(userId, albumId)
    }

    override suspend fun getRankingAtPosition(
        userId: String,
        rankPosition: Int
    ): RankingEntity? {
        return rankingDao.getRankingAtPosition(
            userId,
            rankPosition
        )
    }

    override suspend fun deleteUserRankings(userId: String) {
        rankingDao.deleteUserRankings(userId)
    }

    // --------------------------------------------------
    // Listening history
    // --------------------------------------------------

    override suspend fun insertListening(
        listening: ListeningEntity
    ) {
        listeningDao.insertListening(listening)
    }

    override suspend fun insertListeningItems(
        items: List<ListeningEntity>
    ) {
        listeningDao.insertListenings(items)
    }

    override suspend fun deleteListening(
        listening: ListeningEntity
    ) {
        listeningDao.deleteListening(listening)
    }

    override fun getUserListeningHistory(
        userId: String
    ): Flow<List<ListeningEntity>> {
        return listeningDao.getUserListeningHistory(userId)
    }

    override fun getSongListeningHistory(
        userId: String,
        songId: Long
    ): Flow<List<ListeningEntity>> {
        return listeningDao.getSongListeningHistory(
            userId,
            songId
        )
    }

    override fun getRecentListening(
        userId: String,
        limit: Int
    ): Flow<List<ListeningEntity>> {
        return listeningDao.getRecentListening(
            userId,
            limit
        )
    }

    override suspend fun deleteUserListeningHistory(
        userId: String
    ) {
        listeningDao.deleteUserListeningHistory(userId)
    }

    // --------------------------------------------------
    // Tier list
    // --------------------------------------------------

    override suspend fun insertTierListItem(
        item: TierListItemEntity
    ) {
        tierListItemDao.insertTierListItem(item)
    }

    override suspend fun insertTierListItems(
        items: List<TierListItemEntity>
    ) {
        tierListItemDao.insertTierListItems(items)
    }

    override suspend fun updateTierListItem(
        item: TierListItemEntity
    ) {
        tierListItemDao.updateTierListItem(item)
    }

    override suspend fun deleteTierListItem(
        item: TierListItemEntity
    ) {
        tierListItemDao.deleteTierListItem(item)
    }

    override fun getUserTierListItems(
        userId: String
    ): Flow<List<TierListItemEntity>> {
        return tierListItemDao.getUserTierListItems(userId)
    }

    override fun getItemsByTier(
        userId: String,
        tier: String
    ): Flow<List<TierListItemEntity>> {
        return tierListItemDao.getItemsByTier(
            userId,
            tier
        )
    }

    override fun getItemsByType(
        userId: String,
        itemType: String
    ): Flow<List<TierListItemEntity>> {
        return tierListItemDao.getItemsByType(
            userId,
            itemType
        )
    }

    override suspend fun deleteUserTierListItems(
        userId: String
    ) {
        tierListItemDao.deleteUserTierListItems(userId)
    }
}