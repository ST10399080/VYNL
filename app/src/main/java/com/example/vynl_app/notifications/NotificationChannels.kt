package com.example.vynl_app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.vynl_app.R
import com.example.vynl_app.data.settings.NotificationCategory

// One channel per category, so the user can also mute a category from the system settings.
object NotificationChannels {
    const val RECOMMENDATIONS = "vynl_recommendations"
    const val REVIEWS = "vynl_reviews"
    const val FRIEND_ACTIVITY = "vynl_friend_activity"

    fun idFor(category: NotificationCategory): String = when (category) {
        NotificationCategory.RECOMMENDATIONS -> RECOMMENDATIONS
        NotificationCategory.REVIEWS -> REVIEWS
        NotificationCategory.FRIEND_ACTIVITY -> FRIEND_ACTIVITY
    }

    fun createAll(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannels(
            listOf(
                NotificationChannel(RECOMMENDATIONS, context.getString(R.string.channel_recommendations), NotificationManager.IMPORTANCE_DEFAULT),
                NotificationChannel(REVIEWS, context.getString(R.string.channel_reviews), NotificationManager.IMPORTANCE_DEFAULT),
                NotificationChannel(FRIEND_ACTIVITY, context.getString(R.string.channel_friend_activity), NotificationManager.IMPORTANCE_DEFAULT)
            )
        )
    }
}
