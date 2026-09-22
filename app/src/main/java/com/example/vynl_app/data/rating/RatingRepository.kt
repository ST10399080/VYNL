package com.example.vynl_app.data.rating

import kotlinx.coroutines.flow.Flow

// The contract between UI and database work. UI code only ever talks to
// this interface, never to Room/Firestore directly, so both sides of the
// team can build independently. Your database teammate implements this;
// FakeRatingRepository (next file) is a stand-in you use until then.


interface RatingRepository {
    // A stream of the current user's own rating. "Flow" means the UI
    // automatically updates whenever this value changes, no manual refresh.
    fun observeUserRating (albumId: String): Flow<Int?>

    // One-time fetch of the album's public average. "suspend" means this
    // can do async work (a network/database call) without blocking the UI thread.
    suspend fun getAlbumRatingSummary(albumId: String): AlbumRatingSummary

    // Called when the user taps a star to submit or edit their rating.
    suspend fun submitRating(albumId: String, userId: String, newStars: Int)

}