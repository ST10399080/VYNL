package com.example.vynl_app.data.compatibility

import kotlinx.coroutines.delay

// DATA CLASS: Represents the compatibility result model
data class TasteMatchProfile(
    val userId: String,
    val username: String,
    val handle: String,
    val avatarUrl: String,
    val compatibilityScore: Int, // e.g., 92 (%)
    val sharedArtists: List<String>,
    val sharedGenres: List<String>,
    val differingTaste: String
)

// INTERFACE: Contract for retrieving compatibility data
interface CompatibilityRepository {
    suspend fun getMatchDetails(targetUserId: String): TasteMatchProfile
}

// CLASS: Fake implementation providing sample compatibility data
class FakeCompatibilityRepository : CompatibilityRepository {
    override suspend fun getMatchDetails(targetUserId: String): TasteMatchProfile {
        delay(400) // Simulated network delay
        return when (targetUserId) {
            "elena-rostova" -> TasteMatchProfile(
                userId = "elena-rostova",
                username = "Elena Rostova",
                handle = "@elenarose",
                avatarUrl = "",
                compatibilityScore = 92,
                sharedArtists = listOf("Night Static", "Bloom Atlas", "Neon Drift"),
                sharedGenres = listOf("Ambient Electronic", "Synthwave", "Indie Pop"),
                differingTaste = "You love heavy industrial techno; Elena prefers lo-fi ambient."
            )
            else -> TasteMatchProfile(
                userId = targetUserId,
                username = "Community User",
                handle = "@musicfan",
                avatarUrl = "",
                compatibilityScore = 74,
                sharedArtists = listOf("Night Static", "The Midnight"),
                sharedGenres = listOf("Synthwave", "Alternative Rock"),
                differingTaste = "You lean heavier into atmospheric soundtracks."
            )
        }
    }
}