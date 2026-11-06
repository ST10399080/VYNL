package com.example.vynl_app.data.challenge

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Local daily challenge implementation for the feature branch.
 * It deliberately keeps the API boundary in DailyChallengeRepository because
 * the planning document does not yet define daily-challenge API endpoints.
 */
class FakeDailyChallengeRepository(
    private val preferences: SharedPreferences
) : DailyChallengeRepository {

    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    private val challenge = DailyChallenge(
        id = "music-wordle-$today",
        date = today,
        type = ChallengeType.ALBUM,
        title = "Identify the Album",
        answer = "Midnight Echoes",
        clue = "Released in the 2020s. The title suggests a late-night listening session.",
        metadataFragment = "10 tracks • Pop • community score 91%",
        hints = listOf(
            "The album was released in the 2020s.",
            "The title combines a time of day with repeated sound.",
            "The artist is Luna Vale."
        ),
        artworkUrl = null,
        acceptedAnswers = listOf("midnight echoes", "midnight echoes"),
        maxAttempts = 5
    )

    private val progressState = MutableStateFlow(loadProgress())

    override suspend fun getTodayChallenge(): DailyChallenge = challenge

    override fun observeProgress(challengeId: String): Flow<ChallengeProgress> =
        progressState.asStateFlow()

    override suspend fun submitAnswer(challenge: DailyChallenge, answer: String): ChallengeProgress {
        val current = progressState.value
        if (current.completed) return current

        val normalised = answer.trim().lowercase()
        val solved = challenge.acceptedAnswers.any { it.trim().lowercase() == normalised }
        val attempts = current.attempts + 1
        val completed = solved || attempts >= challenge.maxAttempts
        val next = current.copy(
            attempts = attempts,
            completed = completed,
            solved = solved,
            revealed = completed && !solved,
            score = if (solved) maxOf(10, 100 - (attempts - 1) * 15 - current.hintsRevealed * 10) else current.score,
            streak = if (solved) maxOf(1, current.streak + 1) else current.streak,
            message = if (solved) "Correct! Your streak continues." else if (completed) "Challenge complete. Answer revealed." else "Not quite. Try another answer."
        )
        saveProgress(next)
        progressState.value = next
        return next
    }

    override suspend fun revealNextHint(challenge: DailyChallenge): ChallengeProgress {
        val current = progressState.value

        if (current.completed || current.hintsRevealed >= challenge.hints.size) {
            return current
        }

        val next = current.copy(
            hintsRevealed = current.hintsRevealed + 1,
            message = "Hint ${current.hintsRevealed + 1} revealed."
        )

        saveProgress(next)
        progressState.value = next

        return next
    }

    override suspend fun revealAnswer(challenge: DailyChallenge): ChallengeProgress {
        val next = progressState.value.copy(completed = true, revealed = true, message = "Answer revealed.")
        saveProgress(next)
        progressState.value = next
        return next
    }

    override suspend fun getLeaderboard(): List<LeaderboardEntry> = listOf(
        LeaderboardEntry(1, "VinylHead", 21, 74),
        LeaderboardEntry(2, "MusicFan01", progressState.value.streak, 62),
        LeaderboardEntry(3, "AlbumHunter", 15, 58),
        LeaderboardEntry(4, "RhythmSoul", 11, 49),
        LeaderboardEntry(5, "TrackSeeker", 9, 45)
    )

    private fun loadProgress(): ChallengeProgress {
        val savedId = preferences.getString(KEY_ID, challenge.id) ?: challenge.id
        return if (savedId != challenge.id) {
            ChallengeProgress(challenge.id)
        } else {
            ChallengeProgress(
                challengeId = challenge.id,
                attempts = preferences.getInt(KEY_ATTEMPTS, 0),
                completed = preferences.getBoolean(KEY_COMPLETED, false),
                solved = preferences.getBoolean(KEY_SOLVED, false),
                revealed = preferences.getBoolean(KEY_REVEALED, false),
                hintsRevealed = preferences.getInt(KEY_HINTS, 0),
                score = preferences.getInt(KEY_SCORE, 0),
                streak = preferences.getInt(KEY_STREAK, 0),
                message = preferences.getString(KEY_MESSAGE, null)
            )
        }
    }

    private fun saveProgress(progress: ChallengeProgress) {
        preferences.edit()
            .putString(KEY_ID, progress.challengeId)
            .putInt(KEY_ATTEMPTS, progress.attempts)
            .putBoolean(KEY_COMPLETED, progress.completed)
            .putBoolean(KEY_SOLVED, progress.solved)
            .putBoolean(KEY_REVEALED, progress.revealed)
            .putInt(KEY_HINTS, progress.hintsRevealed)
            .putInt(KEY_SCORE, progress.score)
            .putInt(KEY_STREAK, progress.streak)
            .putString(KEY_MESSAGE, progress.message)
            .apply()
    }

    private companion object {
        const val KEY_ID = "daily.id"
        const val KEY_ATTEMPTS = "daily.attempts"
        const val KEY_COMPLETED = "daily.completed"
        const val KEY_SOLVED = "daily.solved"
        const val KEY_REVEALED = "daily.revealed"
        const val KEY_HINTS = "daily.hints"
        const val KEY_SCORE = "daily.score"
        const val KEY_STREAK = "daily.streak"
        const val KEY_MESSAGE = "daily.message"
    }
}
