package com.example.vynl_app.data.profile

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray

/**
 * Local implementation used by this feature branch until the team's REST API
 * implementation is wired in. It persists profile customisation on-device so
 * the feature can be demonstrated without changing the login/backend branch.
 */
class FakeProfileRepository(
    private val preferences: SharedPreferences,
    private val defaultUserId: String = "demo-user"
) : ProfileRepository {

    private val profileState = MutableStateFlow(loadProfile())

    override fun observeProfile(userId: String): Flow<UserProfile> = profileState.asStateFlow()

    override suspend fun updateProfile(profile: UserProfile) {
        preferences.edit()
            .putString(KEY_USER_ID, profile.userId)
            .putString(KEY_USERNAME, profile.username)
            .putString(KEY_PICTURE, profile.profilePictureUrl)
            .putString(KEY_BIO, profile.biography)
            .putString(KEY_GENRES, JSONArray(profile.favoriteGenres).toString())
            .putString(KEY_ARTISTS, JSONArray(profile.favoriteArtists).toString())
            .putString(KEY_THEME, profile.themePreference.name)
            .putString(KEY_PINNED, JSONArray(profile.pinnedFavoriteAlbums).toString())
            .putInt(KEY_REVIEWS, profile.reviewCount)
            .putInt(KEY_RATINGS, profile.ratingCount)
            .putInt(KEY_FOLLOWERS, profile.followerCount)
            .putInt(KEY_STREAK, profile.listeningStreak)
            .apply()
        profileState.value = profile
    }

    private fun loadProfile(): UserProfile {
        return UserProfile(
            userId = preferences.getString(KEY_USER_ID, defaultUserId) ?: defaultUserId,
            username = preferences.getString(KEY_USERNAME, "MusicFan01") ?: "MusicFan01",
            profilePictureUrl = preferences.getString(KEY_PICTURE, null),
            biography = preferences.getString(KEY_BIO, "Music enthusiast • building my VYNL collection")
                ?: "Music enthusiast • building my VYNL collection",
            favoriteGenres = readList(KEY_GENRES),
            favoriteArtists = readList(KEY_ARTISTS),
            themePreference = runCatching {
                ThemePreference.valueOf(
                    preferences.getString(KEY_THEME, ThemePreference.DARK.name) ?: ThemePreference.DARK.name
                )
            }.getOrDefault(ThemePreference.DARK),
            pinnedFavoriteAlbums = readList(KEY_PINNED),
            reviewCount = preferences.getInt(KEY_REVIEWS, 83),
            ratingCount = preferences.getInt(KEY_RATINGS, 197),
            followerCount = preferences.getInt(KEY_FOLLOWERS, 210),
            listeningStreak = preferences.getInt(KEY_STREAK, 12)
        )
    }

    private fun readList(key: String): List<String> {
        val raw = preferences.getString(key, null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            List(array.length()) { index -> array.getString(index) }
        }.getOrDefault(emptyList())
    }

    private companion object {
        const val KEY_USER_ID = "profile.userId"
        const val KEY_USERNAME = "profile.username"
        const val KEY_PICTURE = "profile.picture"
        const val KEY_BIO = "profile.bio"
        const val KEY_GENRES = "profile.genres"
        const val KEY_ARTISTS = "profile.artists"
        const val KEY_THEME = "profile.theme"
        const val KEY_PINNED = "profile.pinned"
        const val KEY_REVIEWS = "profile.reviews"
        const val KEY_RATINGS = "profile.ratings"
        const val KEY_FOLLOWERS = "profile.followers"
        const val KEY_STREAK = "profile.streak"
    }
}
