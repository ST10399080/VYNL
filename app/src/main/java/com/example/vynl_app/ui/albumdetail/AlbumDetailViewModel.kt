package com.example.vynl_app.ui.albumdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.album.AlbumDetail
import com.example.vynl_app.data.album.AlbumRepository
import com.example.vynl_app.data.rating.AlbumRatingSummary
import com.example.vynl_app.data.rating.RatingRepository
import com.example.vynl_app.data.review.Review
import com.example.vynl_app.data.review.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Only depends on the repository INTERFACES, not concrete classes.
// Pass the Fake* repositories in for now; swap in the real ones later with
// zero changes here.
class AlbumDetailViewModel(
    private val albumId: String,
    private val currentUserId: String,
    private val ratingRepository: RatingRepository,
    private val albumRepository: AlbumRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    // The user's own rating, exposed as a StateFlow so the UI recomposes
    // automatically whenever it changes, no manual refresh needed.
    val userRating: StateFlow<Int?> = MutableStateFlow<Int?>(null).also { flow ->
        viewModelScope.launch { // launches a coroutine tied to this screen's lifecycle
            ratingRepository.observeUserRating(albumId).collect { flow.value = it }
        }
    }.asStateFlow()

    private val _communityScore = MutableStateFlow(AlbumRatingSummary())
    val communityScore: StateFlow<AlbumRatingSummary> = _communityScore.asStateFlow()

    // null until the album has loaded; the screen shows a spinner in the meantime.
    private val _album = MutableStateFlow<AlbumDetail?>(null)
    val album: StateFlow<AlbumDetail?> = _album.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    init {
        loadCommunityScore() // fetch the average as soon as this ViewModel is created
        loadAlbum()
        loadReviews()
    }

    private fun loadCommunityScore() {
        viewModelScope.launch {
            _communityScore.value = ratingRepository.getAlbumRatingSummary(albumId)
        }
    }

    private fun loadAlbum() {
        viewModelScope.launch {
            _album.value = albumRepository.getAlbumDetail(albumId)
        }
    }

    private fun loadReviews() {
        viewModelScope.launch {
            _reviews.value = reviewRepository.getRecentReviews(albumId)
        }
    }

    // Called when the user taps a star.
    fun onRatingSubmitted(stars: Int) {
        viewModelScope.launch {
            ratingRepository.submitRating(albumId, currentUserId, stars)
            loadCommunityScore() // refresh the average to reflect the new rating
        }
    }
}
