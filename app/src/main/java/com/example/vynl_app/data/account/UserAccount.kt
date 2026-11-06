package com.example.vynl_app.data.account

data class UserAccount(
    val id: String = "demo-user",
    val displayName: String = "Demo User",
    val email: String = "user@example.com",
    val avatarUrl: String = ""
)