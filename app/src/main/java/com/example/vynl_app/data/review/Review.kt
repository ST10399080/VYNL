package com.example.vynl_app.data.review

data class Review(
    val id: String,
    val authorName: String,
    val handle: String,
    val avatarUrl: String,
    val stars: Int,
    val text: String,
    // Mirrors user_id / created_timestamp on the User_Reviews entity (design doc, page 43).
    val userId: String,
    val createdAt: Long
)
