package com.example.vynl_app.ui.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.vynl_app.R
import com.example.vynl_app.data.settings.NotificationCategory
import com.example.vynl_app.data.settings.ThemeMode
import com.example.vynl_app.notifications.NotificationHelper
import com.example.vynl_app.ui.language.LanguageManager
import com.example.vynl_app.ui.notifications.openAppNotificationSettings
import com.example.vynl_app.ui.notifications.rememberNotificationPermissionRequest
import com.example.vynl_app.ui.theme.VynlColors

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val accountError by viewModel.accountError.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showAccountDialog by remember { mutableStateOf(false) }
    var currentLanguage by remember { mutableStateOf(LanguageManager.current()) }
    val settings = state.settings

    // Notifications only work when BOTH our setting and the system permission allow them.
    val context = LocalContext.current
    var osAllowed by remember { mutableStateOf(NotificationHelper.canPost(context)) }
    var showBlockedDialog by remember { mutableStateOf(false) }
    // The user may have changed the permission in the system settings while we were away.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { osAllowed = NotificationHelper.canPost(context) }
    val requestPermission = rememberNotificationPermissionRequest { granted ->
        osAllowed = NotificationHelper.canPost(context)
        if (granted) viewModel.onNotificationsToggled(true) else showBlockedDialog = true
    }
    val notificationsActive = settings.notificationsEnabled && osAllowed
    val testTitle = stringResource(R.string.notification_test_title)
    val testBody = stringResource(R.string.notification_test_body)
    val testSent = stringResource(R.string.settings_test_notification_sent)

    Column(
        Modifier
            .fillMaxSize()
            .background(VynlColors.Background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp).padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.action_back),
                    tint = VynlColors.TextPrimary
                )
            }
            Text(
                stringResource(R.string.settings_title),
                modifier = Modifier.padding(start = 8.dp),
                color = VynlColors.TextPrimary,
                fontSize = 24.sp
            )
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsSection(stringResource(R.string.settings_appearance)) {
                ThemeMode.entries.forEach { mode ->
                    SettingsRadioRow(
                        label = themeModeLabel(mode),
                        selected = settings.themeMode == mode,
                        onClick = { viewModel.onThemeSelected(mode) }
                    )
                }
            }

            SettingsSection(stringResource(R.string.settings_language)) {
                SettingsNavRow(
                    label = stringResource(R.string.settings_language),
                    value = currentLanguage.nativeName,
                    onClick = { showLanguageDialog = true }
                )
            }

            SettingsSection(stringResource(R.string.settings_notifications)) {
                SettingsSwitchRow(
                    label = stringResource(R.string.settings_notifications_enable),
                    checked = notificationsActive,
                    onCheckedChange = { enabled ->
                        if (enabled) requestPermission() else viewModel.onNotificationsToggled(false)
                    }
                )
                SettingsSwitchRow(
                    label = stringResource(R.string.settings_notify_recommendations),
                    checked = settings.notifyRecommendations,
                    enabled = notificationsActive,
                    onCheckedChange = { viewModel.onCategoryToggled(NotificationCategory.RECOMMENDATIONS, it) }
                )
                SettingsSwitchRow(
                    label = stringResource(R.string.settings_notify_reviews),
                    checked = settings.notifyReviews,
                    enabled = notificationsActive,
                    onCheckedChange = { viewModel.onCategoryToggled(NotificationCategory.REVIEWS, it) }
                )
                SettingsSwitchRow(
                    label = stringResource(R.string.settings_notify_friend_activity),
                    checked = settings.notifyFriendActivity,
                    enabled = notificationsActive,
                    onCheckedChange = { viewModel.onCategoryToggled(NotificationCategory.FRIEND_ACTIVITY, it) }
                )
                // Lets you (and a marker) see a notification arrive and open Album Detail when tapped.
                SettingsNavRow(
                    label = stringResource(R.string.settings_send_test_notification),
                    value = "",
                    onClick = {
                        if (osAllowed) {
                            NotificationHelper.show(context, testTitle, testBody, NotificationCategory.RECOMMENDATIONS, albumId = "1")
                            Toast.makeText(context, testSent, Toast.LENGTH_LONG).show()
                        } else {
                            showBlockedDialog = true
                        }
                    }
                )
            }

            SettingsSection(stringResource(R.string.settings_account)) {
                SettingsNavRow(
                    label = stringResource(R.string.settings_display_name),
                    value = state.account.displayName,
                    onClick = {
                        viewModel.clearAccountError()
                        showAccountDialog = true
                    }
                )
                SettingsNavRow(
                    label = stringResource(R.string.settings_email),
                    value = state.account.email.ifBlank { stringResource(R.string.settings_not_set) },
                    onClick = {
                        viewModel.clearAccountError()
                        showAccountDialog = true
                    }
                )
            }
        }
    }

    if (showLanguageDialog) {
        LanguageDialog(
            current = currentLanguage,
            onSelected = { language ->
                showLanguageDialog = false
                if (language != currentLanguage) {
                    currentLanguage = language
                    LanguageManager.set(language) // recreates the activity with the new strings
                }
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showAccountDialog) {
        AccountEditDialog(
            initialName = state.account.displayName,
            initialEmail = state.account.email,
            error = accountError,
            onSave = { name, email -> viewModel.onSaveAccount(name, email) { showAccountDialog = false } },
            onDismiss = { showAccountDialog = false }
        )
    }

    if (showBlockedDialog) {
        NotificationsBlockedDialog(
            onOpenSettings = {
                showBlockedDialog = false
                openAppNotificationSettings(context)
            },
            onDismiss = { showBlockedDialog = false }
        )
    }
}
