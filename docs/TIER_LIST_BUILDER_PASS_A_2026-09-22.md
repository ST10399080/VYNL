# Tier List Builder + Viewer — Pass A (2026-09-22)

Planning only. No code written yet. Locked parameters going in: build both a Builder and a read-only Viewer this session (no browser/index screen — that lives on Profile eventually); per-artist, either songs or albums (not mixed), 5 tiers (S/A/B/C/D); tap-to-assign interaction, not drag-and-drop; entry point is Artist Detail's overflow menu ("Rank Albums" / "Rank Songs"); new `data/tierlist/` domain, shared single repository instance per the `AGENTS.md` convention.

## (a) Figma search — one dedicated screen, no Viewer frame

The file has a single page, so this is the complete search, not a partial one:

- **`Tier List Builder`** — `1:2948` (full screen, pulled and reviewed)
- **`Tier List Preview`** — `1:1426` — not a full-screen Viewer. It's a small compact widget: a bordered card showing only the top 3 ranked entries as condensed rows (tier badge, title, year, and a short italic caption like "Peak lyrical cadence" per item). Built for embedding elsewhere (Profile, most likely) as a summary teaser, not a standalone screen.
- **`Article - Tier List / Ranking Card`** — `1:1410` — same category by name as the above (another card-shaped summary component); not pulled in full since `Tier List Preview` already answers the real question.
- **`Button - Share tier list`** — `1:624` — standalone icon button, embedded elsewhere, not a screen.

**No dedicated read-only Viewer screen exists in Figma.** The Viewer is designed from the spec + existing dark-theme conventions. One thing carried over from `Tier List Preview` but *not* built: it introduces a per-item caption field ("Peak lyrical cadence," "Raw breakthrough") that isn't in the design doc's section 13 spec (which only asks for a name, artist, and per-tier item IDs). Same treatment as every other Figma-vs-doc gap this session — omitted, not stubbed.

## (b) File plan — exhaustive

**Add (8 files):**
- `data/tierlist/TierList.kt` — `TierRank` enum, `TierListMode` enum, `TierListItem`, `TierList`
- `data/tierlist/TierListRepository.kt`
- `data/tierlist/FakeTierListRepository.kt`
- `ui/tierlistbuilder/TierListBuilderViewModel.kt`
- `ui/tierlistbuilder/TierListBuilderScreen.kt`
- `ui/tierlistbuilder/TierListBuilderComponents.kt` (tier row, tray card, selected-state styling)
- `ui/tierlistviewer/TierListViewerViewModel.kt`
- `ui/tierlistviewer/TierListViewerScreen.kt` (read-only; expected simple enough not to need its own Components file — split if it crosses ~200 lines per convention)

**Edit (7 files):**
- `data/artist/ArtistTrack.kt` — add `id: String`
- `data/artist/FakeArtistRepository.kt` — supply that `id` for the 3 fake tracks
- `ui/navigation/VynlDestinations.kt` — add `TierListBuilder(artistId: String, mode: String)`, `TierListViewer(tierListId: String)`
- `ui/artistdetail/ArtistDetailComponents.kt` — `ArtistDetailTopBar` gets "Rank Albums"/"Rank Songs" menu items + 2 new callback params
- `ui/artistdetail/ArtistDetailScreen.kt` — thread the 2 new callbacks through
- `MainActivity.kt` — hoist `val tierListRepository = remember { FakeTierListRepository() }`, add 2 new `composable<>` blocks, wire the new Artist Detail callbacks

`mode` is passed through the route as a plain `String` ("ALBUMS"/"SONGS"), not the domain enum directly, keeping routes carrying only primitives — consistent with every other route in the app.

## (c) Core interaction — exact state machine

ViewModel holds one field: `selectedItemId: String?`.

- **Tap an unselected tray item** → selects it. Visual: `VynlColors.Accent` 2dp border + a subtle tinted background replaces the plain `BorderMuted` outline every other card has. The instruction line above the tiers swaps from a static "Tap a card, then tap a tier to place it" to a live "Tap a tier to place **{item title}**."
- **Tap the currently-selected item again** → deselects (toggle off). Instruction line reverts to static.
- **Tap a different item while one is already selected** → selection simply switches to the new item (no move happens; tapping a card never moves *itself*, only tier rows do that).
- **Tap a tier row while an item is selected** → commits: calls `setItemTier(tierListId, selectedItemId, thatTier)`, then clears selection.
- **Tap a tier row with nothing selected** → no-op (tier rows aren't independently tappable).
- **Select an item that's already placed in a tier** (same visual selected-state) → two extra text actions appear next to the instruction line: "Move to Unranked" and "Cancel." Tapping a different tier row moves it there; tapping "Move to Unranked" sets its tier to `null` (back to tray) and clears selection; tapping "Cancel" (or the card again, or its own current tier row) just deselects without changing anything.

Covers all three cases from the original spec (assign from tray, move between tiers, remove to tray) plus an explicit, always-visible way to back out of a selection rather than relying only on "tap the same card again."

## (d) Persistence — auto-save, no Save button

Every tap-to-place/move/remove calls straight into `TierListRepository.setItemTier(...)` and persists immediately — no separate "Save" or "Done" action. Matches the pattern already established for ratings (`onRatingSubmitted`) and Add to List (`onMembershipToggled`): both auto-save on tap, and the design doc's section 2 frames this as the house style ("all settings changes are optimistic in the UI, applying immediately"). The "Preview" button becomes a pure navigation action to the Viewer, not a save-then-navigate combo — nothing is left to save by the time it's tapped. This is also the specific advantage of tap-to-assign over drag-and-drop here: each placement is one discrete, deliberate suspend call, not a continuous gesture needing debouncing or a distinct "drop" event to hook persistence to.

## (e) Seeded fake data — Night Static, Albums mode

```
TierList(
  id = "1", ownerId = "demo-user", artistId = "night-static", mode = ALBUMS,
  title = "Night Static — Albums",
  items = [
    TierListItem("3", S),  // Static Void Phase — 94% rated
    TierListItem("2", A),  // Architectural Echoes — 91%
    TierListItem("1", B),  // Bloom Atlas — 88%
  ]
)
```
Tiers C and D stay empty (visible as empty rows in both Builder and Viewer, matching Figma's own empty-C-tier state in the mock). Order follows the existing community rating percentages from `FakeArtistRepository`, so the seed reads as a coherent ranking rather than an arbitrary one.

## (f) Time estimate — honest, and it's over 4 hours

Roughly **3.5–4.5 hours** for a human developer at a careful, normal pace.

Breakdown:
- Data layer (tier list types, repo, fake, `ArtistTrack.id` extension + verifying nothing else breaks): ~30–45 min
- `TierListBuilderViewModel` (the selection state machine above, candidate loading, mode-agnostic mapping to `TierListDisplayItem`): ~45–60 min
- `TierListBuilderScreen` + `Components` — **the biggest chunk**: 5 tier rows each with distinct tier-color styling (S pink / A yellow / B lime / C gray / D — Figma doesn't even show D's color, needs picking), card selected-state styling, the tray grid, the three-action selected-state UI (move/cancel/tier-tap): ~60–90 min
- `TierListViewerViewModel` + `Screen` (read-only, much simpler): ~30 min
- Navigation wiring (routes, Artist Detail menu, `MainActivity`): ~20–30 min
- Build verification and fixing whatever breaks (this session's track record — the shared-repo bug on Lists — says budget for at least one real catch here): ~20–30 min

The tier-row/selection UI alone is close to a third of the total; it's inherently more stateful than anything else built this session (Add to List's checkboxes are stateless per-row booleans; this has cross-row selection, three conditional actions, and 5 differently-styled containers).
