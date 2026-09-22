package com.example.vynl_app.data.tierlist

enum class TierRank { S, A, B, C, D }

enum class TierListMode { ALBUMS, SONGS }

// tier == null means unranked, sitting in the Builder's tray.
data class TierListItem(
    val itemId: String,
    val tier: TierRank?
)

// Mirrors the shape the design doc (section 13) specifies for tier lists: an owner id, a
// title, and an ordered set of item ids with an added tier value per item.
data class TierList(
    val id: String,
    val ownerId: String,
    val artistId: String,
    val mode: TierListMode,
    val title: String,
    val items: List<TierListItem>
)
