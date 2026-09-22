package com.example.vynl_app.data.review

// Contract for album reviews. Real implementation will read from Firestore.
// UI only ever depends on this interface.
interface ReviewRepository {
    suspend fun getRecentReviews(albumId: String): List<Review>
}
