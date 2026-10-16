# VYNL Feature Branch Integration

Branch purpose: implement **User Defined Feature 3 — Profile Customization** and **Daily Music Wordle / Album Snippet Challenge** without owning the team's login implementation.

## Suggested branch name

`feature/profile-customization-daily-challenge`

## What this branch adds

This implements the GitHub issue requirements for #13 (profile customisation) and #26 (daily Music Wordle / Album Snippet Challenge). The issue #13 completion criteria are profile-picture selection, editable biography, selectable genres/artists, and successful saving. Issue #26 calls for daily generation, guess submission, progressive hints, score tracking, and a leaderboard/streak.

### Profile Customization
- Editable username
- Profile picture picker using Android's image picker
- Biography
- Favourite genres
- Favourite artists
- Theme preference selector (System / Light / Dark)
- Profile statistics display
- Local persistence for demo/offline use
- Repository interface ready for the team's REST API implementation

The planning document defines the profile endpoint as:
- `GET /users/profile/{id}`
- `POST /users/profile`

The profile payload includes username, profile picture, biography, favourite genres and favourite artists.

### Daily Music Wordle / Album Snippet Challenge
- One daily challenge
- Album / artist / song challenge types
- Clue
- Optional metadata clue
- Answer input
- Attempt counter
- Correct-answer handling
- Give-up/reveal flow
- Daily streak
- Leaderboard UI
- Local persistence for demo use

The planning document explicitly notes that daily challenge API endpoints were not yet defined. The feature therefore keeps a repository interface so the API owner can connect it without changing the Compose UI. The current fake implementation provides progressive hints, attempt-based scoring, a streak counter, and leaderboard data for demonstration.

## Login integration

This branch deliberately does **not** implement Firebase login.

In `MainActivity.kt`, replace:

```kotlin
val currentUserId = "demo-user"
```

with the team's authenticated user ID, for example:

```kotlin
val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setContent
```

Use the team's existing auth wrapper if they have one; do not create a second Firebase auth flow.

## REST API integration

The UI depends on `ProfileRepository` and `DailyChallengeRepository`, not Firebase or HTTP directly.

Implement these interfaces with the team's existing API client. Do not create a second API client if the group already has one.

### Profile mapping

`UserProfile` maps to the planned fields:
- `userId`
- `username`
- `profilePictureUrl`
- `biography`
- `favoriteGenres`
- `favoriteArtists`
- `themePreference`

The backend should continue to own persistence and authorization. The Android branch only owns presentation/state and the repository contract.

### Daily challenge mapping

The planning document requires at minimum an endpoint to fetch today's puzzle and an endpoint to submit an answer, but it does not define their paths. Agree on those paths with the API owner before replacing `FakeDailyChallengeRepository`.

A clean contract would be:
- `GET /daily-challenge/today`
- `POST /daily-challenge/answer`
- `GET /daily-challenge/leaderboard`

These are proposed integration paths, not paths defined by the planning document.

## Git workflow

```bash
git checkout -b feature/profile-customization-daily-challenge
git add app/src/main/java/com/example/vynl_app/data/profile \
        app/src/main/java/com/example/vynl_app/data/challenge \
        app/src/main/java/com/example/vynl_app/ui/profile \
        app/src/main/java/com/example/vynl_app/ui/dailychallenge \
        app/src/main/java/com/example/vynl_app/ui/navigation/VynlDestinations.kt \
        app/src/main/java/com/example/vynl_app/ui/search/SearchScreen.kt \
        app/src/main/java/com/example/vynl_app/MainActivity.kt \
        app/build.gradle.kts \
        FEATURE_BRANCH_INTEGRATION.md
git commit -m "feat: add profile customization and daily music challenge"
git push -u origin feature/profile-customization-daily-challenge
```

Then open a pull request into the team's development branch.

## Merge conflict expectation

The most likely shared-file conflicts are:
- `MainActivity.kt`
- `VynlDestinations.kt`
- `SearchScreen.kt`
- `app/build.gradle.kts`

The new feature files under `data/profile`, `data/challenge`, `ui/profile`, and `ui/dailychallenge` are intentionally isolated to make the branch easier to merge.
