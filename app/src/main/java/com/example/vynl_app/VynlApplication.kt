package com.example.vynl_app

import android.app.Application
import com.example.vynl_app.notifications.NotificationChannels

class VynlApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Channels must exist before any notification is posted (Android 8+).
        NotificationChannels.createAll(this)
    }
}
