package com.example.vynl_app.data.review

// Contract for album reviews. Real implementation will read from Firestore.
// UI only ever depends on this interface.
interface ReviewRepository {
    suspend fun getRecentReviews(albumId: String): List<Review>

    // Called when the user posts a review from the Review Composer sheet.
    suspend fun submitReview(albumId: String, userId: String, stars: Int, text: String)
}
