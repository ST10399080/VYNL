package com.example.vynl_app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.account.AccountError
import com.example.vynl_app.data.account.AccountException
import com.example.vynl_app.data.account.AccountRepository
import com.example.vynl_app.data.account.UserAccount
import com.example.vynl_app.data.settings.AppSettings
import com.example.vynl_app.data.settings.NotificationCategory
import com.example.vynl_app.data.settings.SettingsRepository
import com.example.vynl_app.data.settings.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val settings: AppSettings,
    val account: UserAccount
)

// Only depends on the repository INTERFACES, like every other ViewModel in the app.
class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.settings,
        accountRepository.account
    ) { settings, account -> SettingsUiState(settings, account) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState(AppSettings(), accountRepository.account.value)
        )

    private val _accountError = MutableStateFlow<AccountError?>(null)
    val accountError: StateFlow<AccountError?> = _accountError.asStateFlow()

    fun onThemeSelected(mode: ThemeMode) {
        viewModelScope.launch { settingsRepository.setThemeMode(mode) }
    }

    fun onNotificationsToggled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setNotificationsEnabled(enabled) }
    }

    fun onCategoryToggled(category: NotificationCategory, enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setCategoryEnabled(category, enabled) }
    }

    fun clearAccountError() {
        _accountError.value = null
    }

    // onSaved runs only when the repository accepted the input, so the caller can close its dialog.
    fun onSaveAccount(displayName: String, email: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            accountRepository.updateAccount(displayName, email)
                .onSuccess {
                    _accountError.value = null
                    onSaved()
                }
                .onFailure { _accountError.value = (it as? AccountException)?.error }
        }
    }
}
