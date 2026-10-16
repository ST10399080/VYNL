package com.example.vynl_app.data.review

data class Review(
    val id: String,
    val authorName: String,
    val handle: String,
    val avatarUrl: String,
    val stars: Int,
    val text: String
)
