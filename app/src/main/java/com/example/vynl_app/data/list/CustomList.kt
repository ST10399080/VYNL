package com.example.vynl_app.data.list

// Mirrors the Custom_Lists entity (design doc, page 44).
data class CustomList(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val albumIds: List<String>,
    val isPublic: Boolean
)
