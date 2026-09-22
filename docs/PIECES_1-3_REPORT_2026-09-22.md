# Post-Work Report — Pieces 1-3 (2026-09-22)

Covers: Piece 1 (Artist Detail wiring), Piece 2 (Review Composer Sheet), Piece 3 (Add to List Sheet).

## 1. Build status

**Confirmed green.** `./gradlew assembleDebug` — `BUILD SUCCESSFUL`, all 35 tasks up-to-date, no warnings.

## 2. What changed, per piece

**Piece 1 (Artist Detail wiring).** Edited `data/artist/ArtistRelease.kt` and `ArtistTrack.kt` to add an `albumId` field (nullable on `ArtistTrack`, since one fake track — "Obsidian Frame" — is a standalone single with no parent album), and updated `FakeArtistRepository.kt`'s fixture data with real ids. Edited `ui/artistdetail/ArtistDetailComponents.kt` so `ReleaseCard` and `PopularTrackRow` route through those ids, `ArtistDetailScreen.kt` to accept an `onTrackAlbumClick` param and carry `TODO` comments for every remaining stub, and `MainActivity.kt` to wire both callbacks to `navController.navigate(AlbumDetail(albumId))`. Now wired: tapping an Essential Releases card or a Popular Tracks row (except the one single) opens Album Detail for real. Stubs left, each now commented with its intended behavior and doc citation: Follow, Play Radio, Stories, Share, More options.

**Piece 2 (Review Composer).** Added `ui/albumdetail/ReviewComposerSheet.kt`. Edited `data/review/Review.kt` (added `userId`, `createdAt`), `ReviewRepository.kt` (added `submitReview`), `FakeReviewRepository.kt` (now holds a `MutableStateFlow` instead of a static list, so submissions actually persist in-session), `AlbumDetailComponents.kt` (`ReviewsSection` gained the entry-point link), `AlbumDetailViewModel.kt` (added `onReviewSubmitted`), and `AlbumDetailScreen.kt` (sheet state + wiring). Now wired: opening the sheet, picking a star rating, typing up to 500 characters, and tapping "Post Review" actually submits through the fake repository and the new review appears at the top of the list immediately. Stubs/omissions left: the Figma frame's "Sonic Tags" picker is skipped entirely (not stubbed — just absent), and the "Share to Community Feed" toggle is present and flips visually but its state is never read by `onPostReview` (commented as such).

**Piece 3 (Add to List).** Added `data/list/CustomList.kt`, `ListRepository.kt`, `FakeListRepository.kt` (3 fake lists), and `ui/list/AddToListViewModel.kt` + `AddToListSheet.kt`. Edited `AlbumDetailComponents.kt` (new overflow menu on the top bar), `AlbumDetailScreen.kt` (sheet state + wiring), and `AlbumDetailViewModel.kt` (`currentUserId` changed from `private` to a public `val` so the sheet's ViewModel can be built with the same user). Now wired: the ⋮ menu → "Add to List" opens the sheet, checkboxes reflect real membership from the fake data, toggling a checkbox calls the repository and refreshes, and creating a new list both creates it and adds the current album to it in one action. No stubs — everything in this sheet is functionally wired against the fake repo, nothing is decorative.

## 3. Piece 2 — exact entry-point location

Of the three originally-named options (pencil icon / "Write review" button / overflow menu item), the build used the closest match to the second: a small Accent-colored tappable text link reading **"Write a review"**, placed in `ReviewsSection`'s header row next to "Recent Reviews" — not a distinct `Button` component, styled identically to the existing "Read full bio" (Artist Detail) and artist-name (Album Detail) tap-text pattern already in the app. This was proposed explicitly in the piece-2 plan and approved as-is via the entry-point question.

## 4. Piece 3 — premise correction

Piece 3's original brief explicitly specified the create-new-list affordance — verbatim: "Lists show the user's existing custom lists with a checkbox next to each, **plus a "create new list" affordance at the top**." This was not scope creep added independently; it was in the spec from the start, and the proposed plan (approved before implementation) repeated it back before building it.

## 5. New stubs/TODOs

- Artist Detail: `// TODO` comments on Follow, Play Radio, Stories, Share, and More options (added this session, citing design doc sections / nav-diagram nodes / explicit data gaps where the doc itself doesn't define one — e.g. Play Radio has no backend endpoint anywhere).
- Review Composer: Sonic Tags section absent (not stubbed, just not built); Share-to-Community-Feed toggle is inert with a `// TODO` noting no Activity Feed exists yet.

## 6. Decisions made without a separate confirmation — worth double-checking

- **Post Review button is disabled until a star rating is picked** (`enabled = currentRating > 0`). Not specified either way in the Figma frame or the original prose; added as a judgment call.
- **A newly created list auto-adds the current album** (create → immediately checked), rather than creating an empty list requiring a second tap. This was stated in the Piece 3 plan text ("creates and immediately checks a new list") but wasn't a separate yes/no question — bundled into the plan approved as a whole.
- **Checkbox toggling in Add to List re-fetches the entire list after every tap** (150ms fake delay) rather than updating optimistically in local UI state first. This is a real deviation from the design doc's own stated convention (section 2: "All settings changes are optimistic in the UI, applying immediately... synced to the API in the background") — worth revisiting if that pattern should be followed here too.
- **Fake self-submitted reviews display as authorName "You", handle "@{userId}"** — invented display convention, since the Figma frame never shows what a just-posted review card looks like.
- **`AlbumDetailViewModel.currentUserId` visibility changed from `private` to a public `val`** — small API-surface widening, done so `AlbumDetailScreen` could construct `AddToListViewModel` with the same user id instead of hardcoding `"demo-user"` a second time.
