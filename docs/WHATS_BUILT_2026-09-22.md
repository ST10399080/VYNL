# VYNL — What's Built (as of 2026-09-22)

Snapshot of the current app state after this session's work. Supersedes the screen/nav-graph parts of `CODEBASE_AUDIT_2026-09-22.md` (written before navigation existed); that file's design-doc gap analysis (§8) is still accurate for screens not touched this session.

## Screens

| Screen | Status | Notes |
|---|---|---|
| **Search** | Built, wired, start destination | Unified screen: one text field, a `TabRow` with 4 tabs (Albums / Songs / Artists / Users). Albums and Artists tabs navigate to their detail screens on tap; Songs tab is stubbed (no Song Detail screen exists anywhere yet); Users tab shows a static "User search coming soon" empty state — no `data/user/` domain exists on purpose. No Figma frame exists for this screen (see below) — built against existing app conventions (Material3 `TabRow`, plain `OutlinedTextField`) instead. |
| **Album Detail** | Built, wired, reachable from Search and via deep nav | Built against Figma frame `1:346`. Rate album → bottom sheet with `StarRatingBar`; artist name → Artist Detail; track rows and review cards are display-only; Play button is a stub (no playback engine). |
| **Artist Detail** | Built, wired, reachable from Album Detail | Built against Figma frame `1:964`. Hero (verified badge, Follow/Play Radio/Stories), quantitative metrics, biography, Essential Releases gallery (tap is a stub — no reverse nav back into Album Detail yet), Popular Tracks list (play stub), and the frame's one-off "Patch Architecture" note card. No global header/bottom-nav chrome — screen-scoped top bar only, matching Album Detail's precedent. |

Retired: the old standalone Album Search and Song Search screens (and their ViewModels) — folded into the unified Search screen above.

## Navigation

Real navigation now exists (`androidx.navigation.compose` + type-safe `@Serializable` routes), replacing the old hardcoded single-screen `MainActivity`. Routes live in `ui/navigation/VynlDestinations.kt`:

- `Search` (start destination)
- `AlbumDetail(albumId: String)`
- `ArtistDetail(artistId: String)`

`NavController` is hoisted once in `MainActivity`; screens only ever see plain callback lambdas (`onBack`, `onAlbumClick`, etc.), never the controller itself.

## Data layer

| Domain | Files | Used by |
|---|---|---|
| `data/album/` | `Album`, `AlbumDetail` (both now carry `artistId`), `Track`, `SearchRepository`/`FakeSearchRepository`, `AlbumRepository`/`FakeAlbumRepository` | Search (Albums tab), Album Detail |
| `data/song/` | `Song`, `SongSearchRepository`/`FakeSongSearchRepository` | Search (Songs tab) |
| `data/artist/` | `ArtistDetail`, `ArtistRelease`, `ArtistTrack`, `ArtistRepository`/`FakeArtistRepository`; `ArtistSummary`, `ArtistSearchRepository`/`FakeArtistSearchRepository` | Artist Detail; Search (Artists tab) |
| `data/rating/` | `AlbumRatingSummary`, `RatingRepository`/`FakeRatingRepository` | Album Detail |
| `data/review/` | `Review`, `ReviewRepository`/`FakeReviewRepository` | Album Detail |

All fake, in-memory repositories — no network, no auth, no persistence. Every repository is an interface the ViewModels depend on, so swapping in real Firestore/Last.fm-backed implementations later is a one-line change per repo.

## Wired vs. stub, at a glance

**Wired:** Search's text field + tab switching + Albums/Artists result taps; Album Detail's Rate button + star submission + artist-name tap; Artist Detail's back button.

**Stub (present, no-op):** Play buttons (Album Detail, Artist Detail hero, Popular Tracks); Follow and Stories buttons; Share/More-options on Artist Detail's top bar; Essential Releases card taps; Song result taps in Search; "Read full bio" link.

**Missing entirely:** Users tab has no backing data or repository by design (deferred to when Profile screens are built).

## Known project-level notes

Full detail lives in `app/AGENTS.md`, added this session:
- **Design source-of-truth rule**: Figma wins for visual/layout, the design doc wins for behavior/scope, divergences get noted in-code.
- **Temporary icon substitutions**: Artist Detail stands in `Person`/`Face`/`CheckCircle` for `PersonAdd`/`Videocam`/`Verified` (not in `material-icons-core`, and `material-icons-extended` isn't a dependency) — fix when that dependency is added.
- **Known non-issue**: IDE red squiggles after a Gradle dependency change mean "sync the project," not "broken code."

## Against the design doc's Figure 7 nav diagram

3 of 27 nodes now built and wired (Search, Album Detail, Artist Detail), up from 1 of 27 at the start of this session. Everything else — Login/Register, Home Feed, Onboarding, Profile, Settings, Lists, Tier Lists, Taste Compatibility, Daily Challenge, Notifications, Activity Feed, and the rest — is still unbuilt.

## Build & repo state

`./gradlew assembleDebug` builds clean with no warnings as of the last change in this session. Nothing has been committed or pushed — all work sits as uncommitted changes in the working tree, pending the go-ahead on the shared team repo.
