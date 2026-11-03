package com.example.vynl_app.data.settings

enum class ThemeMode { SYSTEM, LIGHT, DARK }

// key is the value carried in a push message's "category" data field and used for FCM topics.
enum class NotificationCategory(val key: String) {
    RECOMMENDATIONS("recommendations"),
    REVIEWS("reviews"),
    FRIEND_ACTIVITY("friend_activity");

    companion object {
        fun fromKey(key: String?): NotificationCategory =
            entries.firstOrNull { it.key == key } ?: RECOMMENDATIONS
    }
}

data class AppSettings(
    // DARK is the default so the app looks exactly as it did before themes existed.
    val themeMode: ThemeMode = ThemeMode.DARK,
    val notificationsEnabled: Boolean = true,
    val notifyRecommendations: Boolean = true,
    val notifyReviews: Boolean = true,
    val notifyFriendActivity: Boolean = true
) {
    fun isCategoryEnabled(category: NotificationCategory): Boolean = when (category) {
        NotificationCategory.RECOMMENDATIONS -> notifyRecommendations
        NotificationCategory.REVIEWS -> notifyReviews
        NotificationCategory.FRIEND_ACTIVITY -> notifyFriendActivity
    }
}
