package com.example.vynl_app.ui.dailychallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.challenge.ChallengeProgress
import com.example.vynl_app.data.challenge.DailyChallenge
import com.example.vynl_app.data.challenge.DailyChallengeRepository
import com.example.vynl_app.data.challenge.LeaderboardEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DailyChallengeUiState(
    val challenge: DailyChallenge? = null,
    val progress: ChallengeProgress = ChallengeProgress("loading"),
    val leaderboard: List<LeaderboardEntry> = emptyList(),
    val answer: String = "",
    val loading: Boolean = true,
    val message: String? = null
)

class DailyChallengeViewModel(
    private val repository: DailyChallengeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DailyChallengeUiState())
    val uiState: StateFlow<DailyChallengeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val challenge = repository.getTodayChallenge()
            _uiState.value = _uiState.value.copy(challenge = challenge, loading = false)
            repository.observeProgress(challenge.id).collect { progress ->
                _uiState.value = _uiState.value.copy(progress = progress)
            }
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(leaderboard = repository.getLeaderboard())
        }
    }

    fun onAnswerChanged(value: String) {
        _uiState.value = _uiState.value.copy(answer = value)
    }

    fun submitAnswer() {
        val challenge = _uiState.value.challenge ?: return
        val answer = _uiState.value.answer
        if (answer.isBlank()) {
            _uiState.value = _uiState.value.copy(message = "Enter an answer first.")
            return
        }
        viewModelScope.launch {
            val progress = repository.submitAnswer(challenge, answer)
            _uiState.value = _uiState.value.copy(answer = "", message = progress.message)
        }
    }

    fun revealNextHint() {
        val challenge = _uiState.value.challenge ?: return
        viewModelScope.launch {
            val progress = repository.revealNextHint(challenge)
            _uiState.value = _uiState.value.copy(message = progress.message)
        }
    }

    fun revealAnswer() {
        val challenge = _uiState.value.challenge ?: return
        viewModelScope.launch {
            val progress = repository.revealAnswer(challenge)
            _uiState.value = _uiState.value.copy(message = progress.message)
        }
    }
}
