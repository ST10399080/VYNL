# VYNL Codebase Audit — 2026-09-22

Read-only audit. No code was changed to produce this report.

## 1. Screens that exist

| Screen | File | ViewModel | Data layer | Reachable from MainActivity | Shows |
|---|---|---|---|---|---|
| **Album Detail** | `ui/albumdetail/AlbumDetailScreen.kt` (+`AlbumDetailComponents.kt`) | `AlbumDetailViewModel.kt` ✅ | ✅ `data/album/AlbumDetail.kt` + `AlbumRepository`/`FakeAlbumRepository`; `data/rating/*`; `data/review/*` | **Yes** — it's the only screen `MainActivity` launches | Album cover, title/artist, rate + play buttons, community/critic score block, track list, recent reviews, and a rating bottom sheet |
| **Album Search** | `ui/search/SearchScreen.kt` | `SearchViewModel` ✅ (lives in `data/album/SearchViewModal.kt` — misplaced/misnamed, see note below) | ✅ `data/album/Album.kt` + `SearchRepository`/`FakeSearchRepository` | **No** — not referenced anywhere outside its own files | A text field + plain `Text` rows of debounced album search results, no styling |
| **Song Search** | `ui/songsearch/SongSearchScreen.kt` | `ui/songsearch/SongSearchViewModel.kt` ✅ | ✅ `data/song/Song.kt` + `SongSearchRepository`/`FakeSongSearchRepository` | **No** — not referenced anywhere outside its own files | Same bare-bones pattern as Album Search, for songs |

Two more files under `ui/` aren't screens: `ui/rating/StarRatingBar.kt` (shared component, see §4) and `ui/rating/StarRatingBarPreview.kt` (an `@Preview` composable, not a real screen).

## 2. Data layer

| Folder | Contents | Used by |
|---|---|---|
| `data/album/` | `Album.kt` (search result shape), `AlbumDetail.kt`, `Track.kt`, `SearchRepository`+`FakeSearchRepository`, `AlbumRepository`+`FakeAlbumRepository`, and `SearchViewModal.kt` | `SearchScreen`/`SearchViewModel` (unreachable) and `AlbumDetailScreen`/`AlbumDetailViewModel` (live) |
| `data/rating/` | `AlbumRatingSummary.kt`, `RatingRepository`+`FakeRatingRepository`, plus a stray `StarRatingBarPreview.kt` (see note) | `AlbumDetailViewModel` |
| `data/review/` | `Review.kt`, `ReviewRepository`+`FakeReviewRepository` | `AlbumDetailViewModel` |
| `data/song/` | `Song.kt`, `SongSearchRepository`+`FakeSongSearchRepository` | `SongSearchViewModel` (unreachable) |

**Two file-hygiene issues worth flagging (read-only note, not fixed):**
- `data/album/SearchViewModal.kt` actually contains `SearchViewModel` (package `ui.search`) — wrong folder for an MVVM ViewModel per the project's own convention, and the filename has a typo ("Modal" vs "Model").
- `data/rating/StarRatingBarPreview.kt` is an **empty stub class** (`class StarRatingBarPreview {}`), unrelated to the real, working `ui/rating/StarRatingBarPreview.kt` preview composable. Looks like a leftover/accidental file — currently dead code with no references.

## 3. MainActivity state

```kotlin
setContent {
    VynlTheme {
        val viewModel: AlbumDetailViewModel = viewModel {
            AlbumDetailViewModel(albumId = "1", currentUserId = "demo-user",
                ratingRepository = FakeRatingRepository(), albumRepository = FakeAlbumRepository(),
                reviewRepository = FakeReviewRepository())
        }
        AlbumDetailScreen(viewModel)
    }
}
```

There is **no navigation** at all — no `NavHost`, no screen-switching logic, no back stack. `MainActivity` hardcodes exactly one screen (`AlbumDetailScreen`) with one hardcoded album (`albumId = "1"`, which the fake repo ignores anyway and always returns "Bloom Atlas"). `AlbumDetailScreen`'s `onBack` and `onSearchClick` callbacks default to empty lambdas — the back arrow and search icon in its top bar render but do nothing. This matches what `app/AGENTS.md` documents: navigation is explicitly deferred until 4+ screens exist.

## 4. Theme + shared components

`ui/theme/Theme.kt` defines:
- `VynlColors` — 11 token colors (Background, Surface, SurfaceVariant, SurfaceMuted, BorderMuted, BorderSubtle, TextPrimary, TextSecondary, TextReview, Accent, OnAccent), matching the AGENTS.md palette exactly.
- `VynlDarkColorScheme` — bridges those tokens onto Material3's `darkColorScheme`.
- `VynlTheme` — wraps content in `MaterialTheme` + a `Surface` painted `VynlColors.Background`. No light theme variant exists (by design — the design doc's dark-only mock, though this conflicts with the design doc's stated "light/dark mode toggle" requirement in Settings, sections 2 and 10 — flagging, not fixing).

Shared components outside a single screen's folder:
- **`ui/rating/StarRatingBar.kt`** — interactive 5-star picker (text-glyph stars, not icons). Used by `AlbumDetailScreen`'s rating bottom sheet. Per `app/AGENTS.md`, this is called out as "load-bearing" — any new screen needing ratings must reuse it.
- **`ui/rating/StarRatingBarPreview.kt`** — an `@Preview`-only composable exercising `StarRatingBar` standalone; not used by any screen, just a design-time preview.

`AlbumDetailComponents.kt` also defines a private `ReadOnlyStars` composable (display-only, non-interactive stars for review cards) — explicitly kept separate from `StarRatingBar`, scoped to Album Detail only, not shared.

## 5. Build state

`./gradlew assembleDebug` — **BUILD SUCCESSFUL** in 2m 5s, 35 tasks (3 executed, 32 up-to-date). No compiler warnings or deprecation notices appeared in the output. Only Gradle's own generic note (`Consider enabling configuration cache...`). Since most tasks were `UP-TO-DATE` from a prior build, only `compileDebugKotlin`, `dexBuilderDebug`, and `packageDebug` actually re-ran this time — if a guaranteed-clean warning check is needed, a `./gradlew clean assembleDebug` would force a full recompile.

## 6. Git state

Still no commits. `git status` shows ~70 files staged (the `.gitignore` fix and `.idea/` untracking from earlier in this session are also staged, on top of the original scaffold). `git log --oneline -10` errors with `fatal: your current branch 'master' does not have any commits yet`. Nothing has been committed or pushed — waiting on the go-ahead already given for this session (team repo check in progress).

## 7. Wired vs. stubbed — interactive elements per screen

**Album Detail** (the only reachable screen):

| Element | Status |
|---|---|
| Back arrow (top bar) | **STUB** — `onBack` defaults to `{}`, no navigation target exists |
| Search icon (top bar) | **STUB** — `onSearchClick` defaults to `{}` |
| "Rate album" button | **WIRED** — opens the bottom sheet (`showRatingSheet = true`) |
| Play button (icon, next to Rate) | **STUB** — `onPlayClick = { /* playback isn't built yet */ }`, explicitly commented as not built |
| Star tap in rating bottom sheet | **WIRED** — calls `viewModel.onRatingSubmitted(it)`, which writes through `RatingRepository.submitRating` and refreshes the community score |
| Bottom sheet dismiss (tap outside / swipe) | **WIRED** — `onDismissRequest` closes it |
| Track rows | **MISSING** — no `clickable`/`onClick` at all; purely display |
| Review cards | **MISSING** — no interaction; purely display |

**Album Search** (unreachable):

| Element | Status |
|---|---|
| Search text field | **WIRED** (functionally) — debounced `onQueryChanged` drives a real (fake) repository search |
| Result rows | **MISSING** — plain `Text`, no `onClick` |

**Song Search** (unreachable): identical pattern to Album Search — text field wired to search, result rows have no tap handler.

## 8. Gaps against the design doc's Figure 7 navigation diagram

The diagram (page 30 of the planning doc) shows these nodes: Splash, Onboarding (Discover/Track/Connect — 3 screens), Login, Register, Home Feed, Search, Charts, Notifications, Album Detail, Artist Detail, Immersive Artist Story View, Review Composer Sheet, Add to List Sheet, List Detail, Lists, Other User's Profile, Taste Compatibility, Profile, Edit Profile, Listening Stats/Wrapped, Tier List Builder, Activity Feed, Daily Challenge and Leaderboard, Settings, Language, Offline and Sync Queue, Notification Preferences — **27 nodes total** (some are bottom sheets, not full screens).

| Status | Screens |
|---|---|
| **Built and wired** | Album Detail (1 of 27) |
| **Built but not wired / not started against Figma** | Album Search, Song Search — exist as code but aren't in the nav diagram by name at all (the diagram has one "Search" node, presumably a global search covering albums/artists/tracks/users per section 7's requirement — these two screens are a narrower, earlier version of that idea and don't yet match the doc's "single search bar, tabbed results" spec) |
| **Not started** | Splash, Onboarding ×3, Login, Register, Home Feed, Charts, Notifications, Artist Detail, Immersive Artist Story View, Review Composer Sheet, Add to List Sheet, List Detail, Lists, Other User's Profile, Taste Compatibility, Profile, Edit Profile, Listening Stats/Wrapped, Tier List Builder, Activity Feed, Daily Challenge and Leaderboard, Settings, Language, Offline and Sync Queue, Notification Preferences |

That's 1 of 27 nav-diagram nodes built and functional, 2 extra unreachable screens outside the diagram's naming, and 24 nodes with zero code. `app/AGENTS.md`'s own "Next up" note confirms this — it says Artist Detail is next, then the rest "in whatever order the deadlines require."

Also worth noting against the design doc directly: none of the Firebase Auth, Firestore, RoomDB, WorkManager sync, FCM, or Last.fm/REST API integration described in sections 1–5 exists yet — everything today runs on in-memory fake repositories, which `app/AGENTS.md` states is intentional and deferred.
