package com.example.vynl_app.data.rating

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Fake, in-memory implementation of RatingRepository. Nothing here touches
// Room or Firestore, so this lets you build and preview screens today,
// before your teammate's real database code exists. Swap it out later
// with zero changes to the UI, since both implement the same interface.

    class FakeRatingRepository(
        initialUserRating: Int? = null,
        // ~88%, matching the Community Score shown in the Figma mock
        initialSummary: AlbumRatingSummary = AlbumRatingSummary(ratingSum = 220, ratingCount = 50)
    ) : RatingRepository {

        // MutableStateFlow = an observable "box" holding the current value.
        // Anything collecting it gets notified the instant it changes.
        private val userRating = MutableStateFlow(initialUserRating)
        private val summary = MutableStateFlow(initialSummary)

        override fun observeUserRating(albumId: String): StateFlow<Int?> = userRating

        override suspend fun getAlbumRatingSummary(albumId: String): AlbumRatingSummary = summary.value

        override suspend fun submitRating(albumId: String, userId: String, newStars: Int) {
            val previous = userRating.value ?: 0
            val current = summary.value

            // How much the sum changes by: if editing 3 stars to 5, delta is +2.
            // If this is a brand new rating (previous was null, treated as 0),
            // delta just equals the new value.
            val delta = newStars - previous

            // Only increase the count on a first-time rating, not an edit.
            val newCount = if (userRating.value == null) current.ratingCount + 1 else current.ratingCount

            userRating.value = newStars
            summary.value = current.copy(
                ratingSum = current.ratingSum + delta,
                ratingCount = newCount
            )
        }
    }
