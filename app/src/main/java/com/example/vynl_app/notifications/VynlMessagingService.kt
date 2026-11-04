package com.example.vynl_app.notifications

import android.util.Log
import com.example.vynl_app.data.settings.DataStoreSettingsRepository
import com.example.vynl_app.data.settings.NotificationCategory
import com.example.vynl_app.data.settings.settingsDataStore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// Receives Firebase Cloud Messaging pushes. Messages are sent as DATA messages
// (title / body / category / albumId in the data payload) so this runs whether the app is in
// the foreground or background, which lets us honour the user's notification settings.
class VynlMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // TODO: upload to the backend / Firestore user document once accounts exist.
        Log.d(PushRegistrar.TAG, "New FCM token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val category = NotificationCategory.fromKey(data["category"])

        // Runs on a background thread; a single DataStore read is quick.
        val settings = runBlocking {
            DataStoreSettingsRepository(applicationContext.settingsDataStore).settings.first()
        }
        if (!settings.notificationsEnabled || !settings.isCategoryEnabled(category)) return

        val title = data["title"] ?: message.notification?.title ?: return
        val body = data["body"] ?: message.notification?.body.orEmpty()
        NotificationHelper.show(applicationContext, title, body, category, data["albumId"])
    }
}
