package com.example.vynl_app.data.challenge

/**
 * Daily Music Wordle / Album Snippet Challenge.
 * The challenge can target an album, artist, or song as required by the PoE.
 */
data class DailyChallenge(
    val id: String,
    val date: String,
    val type: ChallengeType,
    val title: String,
    val answer: String,
    val clue: String,
    val metadataFragment: String,
    val hints: List<String> = emptyList(),
    val artworkUrl: String? = null,
    val acceptedAnswers: List<String> = listOf(answer),
    val maxAttempts: Int = 5
)

enum class ChallengeType {
    ALBUM,
    ARTIST,
    SONG
}

data class ChallengeProgress(
    val challengeId: String,
    val attempts: Int = 0,
    val completed: Boolean = false,
    val solved: Boolean = false,
    val revealed: Boolean = false,
    val hintsRevealed: Int = 0,
    val score: Int = 0,
    val streak: Int = 0,
    val message: String? = null
)

data class LeaderboardEntry(
    val rank: Int,
    val username: String,
    val currentStreak: Int,
    val correctAnswers: Int
)
