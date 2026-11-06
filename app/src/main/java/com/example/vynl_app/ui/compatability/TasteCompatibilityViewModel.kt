package com.example.vynl_app.ui.compatibility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.compatibility.CompatibilityRepository
import com.example.vynl_app.data.compatibility.TasteMatchProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// DATA CLASS: State wrapper for UI rendering
data class TasteCompatibilityUiState(
    val isLoading: Boolean = true,
    val profile: TasteMatchProfile? = null,
    val errorMessage: String? = null
)

// CLASS: ViewModel extending Android Lifecycle ViewModel
class TasteCompatibilityViewModel(
    private val targetUserId: String,
    private val repository: CompatibilityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasteCompatibilityUiState())
    val uiState: StateFlow<TasteCompatibilityUiState> = _uiState.asStateFlow()

    init {
        loadCompatibility()
    }

    fun loadCompatibility() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val data = repository.getMatchDetails(targetUserId)
                _uiState.value = TasteCompatibilityUiState(isLoading = false, profile = data)
            } catch (e: Exception) {
                _uiState.value = TasteCompatibilityUiState(isLoading = false, errorMessage = e.localizedMessage)
            }
        }
    }
}