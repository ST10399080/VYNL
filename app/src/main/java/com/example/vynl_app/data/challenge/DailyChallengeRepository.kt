package com.example.vynl_app.data.challenge

import kotlinx.coroutines.flow.Flow

interface DailyChallengeRepository {
    suspend fun getTodayChallenge(): DailyChallenge
    fun observeProgress(challengeId: String): Flow<ChallengeProgress>
    suspend fun submitAnswer(challenge: DailyChallenge, answer: String): ChallengeProgress
    suspend fun revealNextHint(challenge: DailyChallenge): ChallengeProgress
    suspend fun revealAnswer(challenge: DailyChallenge): ChallengeProgress
    suspend fun getLeaderboard(): List<LeaderboardEntry>
}
