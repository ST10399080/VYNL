package com.example.vynl_app.data.review

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

// In-memory fake, seeded with the single review shown in the Album Detail Figma frame.
// New reviews from the composer sheet are prepended, same album regardless of albumId,
// matching the simplification every other fake repo in this app already makes.
class FakeReviewRepository : ReviewRepository {

    private val reviews = MutableStateFlow(
        listOf(
            Review(
                id = "1",
                authorName = "Elena Rostova",
                handle = "@elenarose",
                avatarUrl = "",
                stars = 4,
                text = "\"Night Static truly outdid themselves with Bloom Atlas. The progression from " +
                    "Hyperbloom into Neon Drift is seamless, weaving intricate textures over a " +
                    "relentless pulse. A masterclass in atmosphere from start to finish.\"",
                userId = "elena-rostova",
                createdAt = 0L
            )
        )
    )

    override suspend fun getRecentReviews(albumId: String): List<Review> {
        delay(300) // fake network delay
        return reviews.value
    }

    override suspend fun submitReview(albumId: String, userId: String, stars: Int, text: String) {
        delay(300)
        val review = Review(
            id = "local-${System.currentTimeMillis()}",
            authorName = "You",
            handle = "@$userId",
            avatarUrl = "",
            stars = stars,
            text = text,
            userId = userId,
            createdAt = System.currentTimeMillis()
        )
        reviews.value = listOf(review) + reviews.value
    }
}
