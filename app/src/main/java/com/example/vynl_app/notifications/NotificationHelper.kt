package com.example.vynl_app.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.vynl_app.MainActivity
import com.example.vynl_app.R
import com.example.vynl_app.data.settings.NotificationCategory

object NotificationHelper {
    // Intent extra carrying the album to open when the notification is tapped.
    const val EXTRA_ALBUM_ID = "extra_album_id"
    const val ACTION_OPEN_ALBUM = "com.example.vynl_app.OPEN_ALBUM"

    // True when the app may currently post notifications (permission granted on Android 13+,
    // and not switched off for the app in the system settings).
    fun canPost(context: Context): Boolean {
        val permissionGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        return permissionGranted && NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    // Used both by the messaging service (real pushes) and the Settings "test notification" row.
    @SuppressLint("MissingPermission") // canPost() performs the permission check
    fun show(
        context: Context,
        title: String,
        body: String,
        category: NotificationCategory,
        albumId: String? = null
    ) {
        if (!canPost(context)) return

        val openApp = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            action = ACTION_OPEN_ALBUM
            albumId?.let {
                putExtra(EXTRA_ALBUM_ID, it)
                data = Uri.parse("vynl://album/$it") // makes each album's PendingIntent unique
            }
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            (albumId ?: title).hashCode(),
            openApp,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(context, NotificationChannels.idFor(category))
            .setSmallIcon(R.drawable.ic_stat_vynl)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
    }
}
