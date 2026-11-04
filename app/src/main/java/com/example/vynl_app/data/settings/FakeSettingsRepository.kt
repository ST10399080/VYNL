package com.example.vynl_app.data.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

// In-memory version for previews and unit tests.
class FakeSettingsRepository(initial: AppSettings = AppSettings()) : SettingsRepository {
    private val state = MutableStateFlow(initial)
    override val settings: Flow<AppSettings> = state

    override suspend fun setThemeMode(mode: ThemeMode) = state.update { it.copy(themeMode = mode) }

    override suspend fun setNotificationsEnabled(enabled: Boolean) =
        state.update { it.copy(notificationsEnabled = enabled) }

    override suspend fun setCategoryEnabled(category: NotificationCategory, enabled: Boolean) =
        state.update {
            when (category) {
                NotificationCategory.RECOMMENDATIONS -> it.copy(notifyRecommendations = enabled)
                NotificationCategory.REVIEWS -> it.copy(notifyReviews = enabled)
                NotificationCategory.FRIEND_ACTIVITY -> it.copy(notifyFriendActivity = enabled)
            }
        }
}
