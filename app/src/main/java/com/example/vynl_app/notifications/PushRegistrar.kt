package com.example.vynl_app.notifications

import android.content.Context
import android.util.Log
import com.example.vynl_app.data.settings.AppSettings
import com.example.vynl_app.data.settings.NotificationCategory
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

// Thin wrapper around Firebase Messaging that is safe to call when google-services.json is
// missing (e.g. a teammate's or CI build): everything simply does nothing in that case.
object PushRegistrar {
    const val TAG = "VYNL_FCM"

    fun isConfigured(context: Context): Boolean = FirebaseApp.getApps(context).isNotEmpty()

    // Prints the device token to Logcat so a test message can be sent from the Firebase console.
    // Remove (or gate on debug builds) before a release.
    fun logToken(context: Context) {
        if (!isConfigured(context)) {
            Log.w(TAG, "Firebase is not configured (google-services.json missing) — push is inactive")
            return
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) Log.d(TAG, "FCM token: ${task.result}")
            else Log.w(TAG, "Could not fetch FCM token", task.exception)
        }
    }

    // Keeps the device subscribed to exactly the topics the user has switched on, so a console
    // campaign targeted at a topic (e.g. "reviews") only reaches people who want it.
    fun syncTopics(context: Context, settings: AppSettings) {
        if (!isConfigured(context)) return
        val messaging = FirebaseMessaging.getInstance()
        NotificationCategory.entries.forEach { category ->
            if (settings.notificationsEnabled && settings.isCategoryEnabled(category)) {
                messaging.subscribeToTopic(category.key)
            } else {
                messaging.unsubscribeFromTopic(category.key)
            }
        }
    }
}
