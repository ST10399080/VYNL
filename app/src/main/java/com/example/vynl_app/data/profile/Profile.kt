package com.example.vynl_app.data.profile

/**
 * User-owned profile customisation data.
 *
 * The REST API team can map this model to the existing
 * POST /users/profile and GET /users/profile/{id} endpoints.
 */
data class UserProfile(
    val userId: String,
    val username: String,
    val profilePictureUrl: String? = null,
    val biography: String = "",
    val favoriteGenres: List<String> = emptyList(),
    val favoriteArtists: List<String> = emptyList(),
    val themePreference: ThemePreference = ThemePreference.DARK,
    val pinnedFavoriteAlbums: List<String> = emptyList(),
    val reviewCount: Int = 0,
    val ratingCount: Int = 0,
    val followerCount: Int = 0,
    val listeningStreak: Int = 0
)

enum class ThemePreference {
    SYSTEM,
    LIGHT,
    DARK
}
