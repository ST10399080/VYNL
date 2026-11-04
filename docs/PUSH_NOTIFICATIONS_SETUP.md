# Push Notifications (Issue #18) – Setup & Demo

## How it works
- `VynlMessagingService` receives **data** messages from Firebase Cloud Messaging (FCM).
- It checks the user's Settings (master switch + per-category switch) and, if allowed, shows a notification on the matching channel (Recommendations / Reviews / Friend activity).
- Tapping a notification opens **Album Detail** for the `albumId` in the payload (`MainActivity` handles it in `onCreate` / `onNewIntent`).
- The device subscribes to FCM topics `recommendations`, `reviews`, `friend_activity` according to the Settings switches (`PushRegistrar.syncTopics`).
- Android 13+: `POST_NOTIFICATIONS` is requested when the user turns notifications on in Settings. If denied, a dialog sends them to system settings.

## One-time Firebase setup (do NOT commit google-services.json)
1. Firebase console → Add project → **Add Android app**, package name `com.example.vynl_app` (check `applicationId` in `app/build.gradle.kts`).
2. Download `google-services.json` into `app/` (it is git-ignored). Without it the app still builds; push is simply disabled.
3. Sync Gradle, run on an emulator with Google Play services (or a real phone).
4. Logcat filter `VYNL_FCM` prints the device token.
5. CI (issue #14): store the file as a secret and write it to `app/google-services.json` before building.

## Demo / POE evidence
1. Settings → Notifications on → accept permission. Tap **Send test notification** → tap it → Album Detail opens.
2. Firebase console → Messaging → new campaign → Notification isn't used; instead use **Send test message** with the token from Logcat, or send to a topic. Because the app handles *data* messages, use the FCM HTTP v1 API / "Custom data" fields:
   - `title`, `body`, `category` (`recommendations` | `reviews` | `friend_activity`), `albumId` (e.g. `1`).
3. Turn a category off in Settings and resend: nothing is shown.
4. Screenshots to capture: permission prompt, notification in shade, Album Detail after tap, Settings switches, blocked dialog.

## Known notes
- Firebase BoM / google-services plugin versions in `libs.versions.toml` should be bumped to latest stable in Android Studio.
- Settings are read with `runBlocking` inside the messaging service (short, on a background binder thread).
