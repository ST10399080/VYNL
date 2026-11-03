package com.example.vynl_app.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

// Declared once, at top level: DataStore must never have two instances for the same file.
// Also used by the push messaging service, which has no Compose/ViewModel around it.
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "vynl_settings")

// Persists user settings on-device (Preferences DataStore). Changes apply immediately because
// every screen observes the Flow, matching the design doc's "optimistic settings" convention.
class DataStoreSettingsRepository(private val dataStore: DataStore<Preferences>) : SettingsRepository {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val NOTIFY_RECOMMENDATIONS = booleanPreferencesKey("notify_recommendations")
        val NOTIFY_REVIEWS = booleanPreferencesKey("notify_reviews")
        val NOTIFY_FRIEND_ACTIVITY = booleanPreferencesKey("notify_friend_activity")
    }

    override val settings: Flow<AppSettings> = dataStore.data
        .catch { error -> if (error is IOException) emit(emptyPreferences()) else throw error }
        .map { prefs ->
            val defaults = AppSettings()
            AppSettings(
                themeMode = prefs[Keys.THEME_MODE]
                    ?.let { saved -> ThemeMode.entries.firstOrNull { it.name == saved } }
                    ?: defaults.themeMode,
                notificationsEnabled = prefs[Keys.NOTIFICATIONS_ENABLED] ?: defaults.notificationsEnabled,
                notifyRecommendations = prefs[Keys.NOTIFY_RECOMMENDATIONS] ?: defaults.notifyRecommendations,
                notifyReviews = prefs[Keys.NOTIFY_REVIEWS] ?: defaults.notifyReviews,
                notifyFriendActivity = prefs[Keys.NOTIFY_FRIEND_ACTIVITY] ?: defaults.notifyFriendActivity
            )
        }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.NOTIFICATIONS_ENABLED] = enabled }
    }

    override suspend fun setCategoryEnabled(category: NotificationCategory, enabled: Boolean) {
        val key = when (category) {
            NotificationCategory.RECOMMENDATIONS -> Keys.NOTIFY_RECOMMENDATIONS
            NotificationCategory.REVIEWS -> Keys.NOTIFY_REVIEWS
            NotificationCategory.FRIEND_ACTIVITY -> Keys.NOTIFY_FRIEND_ACTIVITY
        }
        dataStore.edit { it[key] = enabled }
    }
}
