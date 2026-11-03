package com.example.vynl_app.ui.notifications

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings as AndroidSettings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.example.vynl_app.notifications.NotificationHelper

// Returns a function that makes sure notifications are allowed, then reports the outcome.
// Android 12 and older need no runtime permission, so the answer comes back immediately.
@Composable
fun rememberNotificationPermissionRequest(onResult: (granted: Boolean) -> Unit): () -> Unit {
    val context = LocalContext.current
    val latestOnResult by rememberUpdatedState(onResult)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        latestOnResult(granted && NotificationHelper.canPost(context))
    }
    return {
        if (NotificationHelper.canPost(context)) {
            latestOnResult(true)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            latestOnResult(false) // switched off in system settings; only the user can fix that
        }
    }
}

// Shown when the permission was denied (Android stops asking after a denial), so the only way
// forward is the app's notification page in the system settings.
fun openAppNotificationSettings(context: Context) {
    val intent = Intent(AndroidSettings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(AndroidSettings.EXTRA_APP_PACKAGE, context.packageName)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}
