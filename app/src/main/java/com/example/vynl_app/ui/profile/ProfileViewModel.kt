package com.example.vynl_app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.profile.ProfileRepository
import com.example.vynl_app.data.profile.ThemePreference
import com.example.vynl_app.data.profile.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val message: String? = null
)

class ProfileViewModel(
    private val userId: String,
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeProfile(userId).collect { profile ->
                _uiState.value = _uiState.value.copy(profile = profile)
            }
        }
    }

    fun startEditing() = _uiState.value.profile?.let {
        _uiState.value = _uiState.value.copy(isEditing = true, message = null)
    }

    fun cancelEditing() = _uiState.value.profile?.let {
        _uiState.value = _uiState.value.copy(isEditing = false, message = null)
    }

    fun saveProfile(
        username: String,
        biography: String,
        profilePictureUrl: String?,
        favoriteGenres: List<String>,
        favoriteArtists: List<String>,
        themePreference: ThemePreference
    ) {
        val current = _uiState.value.profile ?: return
        val updated = current.copy(
            username = username.trim(),
            biography = biography.trim(),
            profilePictureUrl = profilePictureUrl,
            favoriteGenres = favoriteGenres,
            favoriteArtists = favoriteArtists,
            themePreference = themePreference
        )
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, message = null)
            repository.updateProfile(updated)
            _uiState.value = _uiState.value.copy(isEditing = false, isSaving = false, message = "Profile saved")
        }
    }

    fun clearMessage() = _uiState.value.copy(message = null).also { _uiState.value = it }
}
