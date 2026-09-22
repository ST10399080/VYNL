# VYNL — Android app

VYNL is a music discovery, rating, and community app. Native Android, Kotlin + Jetpack Compose + Material3, MVVM. Team project (three developers); I own the Android build (GR3). Full spec: `docs/OPSC6312_PART_1_PLANNING_AND_DESIGN.pdf` (planning) and `docs/OPSC6312_PART_1_RESEARCH.pdf` (research). Figma: https://www.figma.com/design/Wwi7HvQWCtkX9hE8VCmJZR/VYNL

## Current status

Early scaffold. Working: album search flow (`ui/search/`), song search flow (`ui/songsearch/`), Album Detail built against Figma frame `1:346` (`ui/albumdetail/`, with the star-rating bottom sheet from `ui/rating/StarRatingBar.kt`), and the dark VYNL theme with the `VynlColors` palette (`ui/theme/Theme.kt`). Data layer uses fake in-memory repositories — no network, no auth. `MainActivity` currently launches `AlbumDetailScreen` (the search screens still exist but aren't reachable until navigation lands).

Next up: rebuild Artist Detail against the Figma (Album Detail is done). After that, the rest of the design doc's screens (Login/Register, Home Feed, Profile, Settings, Tier Lists, Taste Compatibility, Daily Challenge, Lists, Notifications, Activity Feed, Onboarding) in whatever order the deadlines require.

## Architecture conventions (follow these when adding anything new)

MVVM. Every feature is one folder under `ui/<feature>/` (a `Screen.kt` + a `ViewModel.kt`), talking to one folder under `data/<feature>/` (a data class + an interface repository + a `Fake<Name>Repository` implementation). When a screen file would exceed ~200 lines, split its sub-composables into a sibling `ui/<feature>/<Feature>Components.kt` (e.g. `AlbumDetailComponents.kt`) — the `Screen.kt` keeps the state wiring and layout order, the Components file holds the building blocks. Don't create other extra files per feature without a reason.

Interfaces are what the ViewModels depend on, so swapping in real Firestore/Firebase Auth later is a one-line change per repo. Do not skip the interface even for a fake — the pattern is what makes the swap cheap later.

`StateFlow` for reactive state, `viewModelScope.launch` for anything async, `suspend` on repository methods. Canonical examples already in the repo:

- `data/album/SearchRepository.kt` + `data/album/FakeSearchRepository.kt`
- `data/rating/RatingRepository.kt` + `data/rating/FakeRatingRepository.kt`
- `ui/albumdetail/AlbumDetailViewModel.kt`

## Design tokens (Figma)

The Figma is dark themed and `Theme.kt` ships the dark VYNL scheme (always dark, no light variant). Screens pull tokens from `VynlColors` in `Theme.kt`. Do not hardcode hex values in screens — for a translucent variant use `VynlColors.X.copy(alpha = ...)`.

Palette from Figma (the `VynlColors` object in `Theme.kt`):

- `Background` `#080808` — page background
- `Surface` `#181818` — cards, stat containers
- `SurfaceVariant` `#1E1E1E` — icon-button backgrounds, elevated bits
- `SurfaceMuted` `#1C1B1B` — muted callout cards
- `BorderMuted` `#373737` — 1px card borders and dividers between track rows (the Album Detail frame draws track dividers in solid `#373737`)
- `BorderSubtle` `#66373737` (40% alpha) — not used by any screen yet
- `TextPrimary` white, `TextSecondary` `#959595`, `TextReview` `#C4C7C8`
- `Accent` `#1D9BF0` — links, artist names, index dot
- `OnAccent` `#2F3131` — dark text on the white "Play Radio" button

## Icons

Only `androidx.compose.material.icons.filled.*` and `androidx.compose.material.icons.automirrored.filled.*` are available. Do **NOT** import from `androidx.compose.material.icons.outlined.*` — that requires `material-icons-extended`, which is not in `build.gradle.kts`. For an "unfilled" star, use `Icons.Filled.Star` with `tint = ...copy(alpha = 0.25f)`.

## Images

No image loading library yet. When adding one: `implementation("io.coil-kt:coil-compose:2.7.0")` in `app/build.gradle.kts`, then use `AsyncImage(model = url, contentScale = ContentScale.Crop, ...)`. Until then, cover art and hero images stand in as placeholder gradient `Box`es.

## Figma workflow

The Figma MCP tools give design context on demand: `mcp__Figma__get_metadata` lists all frames in the file, `mcp__Figma__get_design_context` returns reference React code + a screenshot for one frame. When building a new screen, always pull the design context first before writing Compose.

Known frame IDs in the VYNL file (from an earlier metadata pass — verify with `get_metadata` if any look wrong):

- Album Detail: `1:346`
- Artist Detail: `1:964`

For any frame not listed above, call `mcp__Figma__get_metadata` on the root to find its ID before pulling design context.

## Design source-of-truth

When Figma and the design doc diverge, Figma wins for visual/layout decisions, the design doc wins for behavior/scope decisions. Note any divergence in the screen's implementation comments.

## Gradle notes

Compose BOM `2024.09.00`, Kotlin `2.0.21`, `minSdk = 24`, `targetSdk = 36`. When adding dependencies, prefer editing `gradle/libs.versions.toml` and referencing via `libs.foo` in `app/build.gradle.kts`.

## The rating flow is load-bearing

`data/rating/` (RatingRepository, FakeRatingRepository, AlbumRatingSummary) and `ui/rating/StarRatingBar.kt` were written by the team. Any new Album Detail must reuse `StarRatingBar` inside a `ModalBottomSheet` — do not rewrite the rating flow, extend around it.

## When adding a new screen

1. Pull the Figma design context for that frame first (see Figma workflow above)
2. Data model → repo interface → fake repo, all under `data/<feature>/`
3. ViewModel under `ui/<feature>/`, exposing `StateFlow` for anything the UI reads
4. Screen composable, tokens from `VynlColors`, no hardcoded hex, no outlined icons
5. Wire into `MainActivity` for now; real navigation (androidx.navigation.compose) is deferred until we have 4+ screens

## Firebase, backend, network

None yet. Per the design doc: Firebase Authentication for auth, Firestore for user data, RoomDB for offline queue, Last.fm API (proxied through a team-built REST API) for music metadata. All deferred until the screens are shipped against fake repos.

## Known non-issues

- Red squiggles in files referencing new dependencies after a gradle change → File → Sync Project with Gradle Files, not a real error.

## Temporary icon substitutions

`material-icons-extended` isn't a dependency yet (see Icons above), so some screens stand in a base-set filled icon for one the mock actually uses. Fix these when that dependency is eventually added:

- Artist Detail: `Person` stands in for `PersonAdd` (Follow affordance), `Face` stands in for `Videocam` (Stories affordance). Both will confuse users until swapped.
- Artist Detail: `CheckCircle` stands in for `Verified` (the verified-artist badge).

## Known deferred fixes

- Add to List (`ui/list/AddToListViewModel.kt`) re-fetches the whole list after every checkbox toggle instead of updating optimistically. Deviates from the design doc's own stated convention (section 2: "All settings changes are optimistic in the UI, applying immediately... synced to the API in the background"). Revisit when we standardize the mutation-response pattern across repositories.
- Fake self-submitted reviews (`data/review/FakeReviewRepository.kt`) display `authorName = "You"`, `handle = "@{userId}"`. Replace with the real user's profile display name once auth lands.
- `AlbumDetailViewModel.currentUserId` is public (was private) so `AlbumDetailScreen` can construct `AddToListViewModel` with the same user id. When real auth lands, replace this with a shared `UserSession` source of truth and re-privatize the field.

## Deliberately deferred features

Not unfinished work — confirmed out of scope for now because the design doc doesn't require them, even though the Figma mock shows them:

- Review Composer's "Sonic Tags" picker (Figma frame `1:669`). Section 8 ("Ratings and reviews") only specifies "a quick 1-to-5-star rating and an optional written review per album" — no tagging. No tag data domain exists anywhere else in the app either (genre/tag browsing per section 7.3 is a separate, also-unbuilt feature). Treat as a nice-to-have the Figma designer added, not an MVP requirement.
- Home Feed's bottom nav bar (Home/Discover/Activity/Profile). Home Feed is standalone for now — three of the four destinations don't exist yet (only Home itself does). Building the nav bar is its own scope once Discover, Activity, and Profile exist; don't add it piecemeal pointing at missing screens.
- Home Feed's other three tabs (Popular/Trending/Favorites) — only "Top" is built. No algorithmic backend exists to make the other three mean anything different from each other yet.