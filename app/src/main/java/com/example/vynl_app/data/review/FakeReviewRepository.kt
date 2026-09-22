package com.example.vynl_app.data.review

import kotlinx.coroutines.delay

// In-memory fake with the single review shown in the Album Detail Figma frame.
class FakeReviewRepository : ReviewRepository {

    override suspend fun getRecentReviews(albumId: String): List<Review> {
        delay(300) // fake network delay
        return listOf(
            Review(
                id = "1",
                authorName = "Elena Rostova",
                handle = "@elenarose",
                avatarUrl = "",
                stars = 4,
                text = "\"Night Static truly outdid themselves with Bloom Atlas. The progression from " +
                    "Hyperbloom into Neon Drift is seamless, weaving intricate textures over a " +
                    "relentless pulse. A masterclass in atmosphere from start to finish.\""
            )
        )
    }
}
