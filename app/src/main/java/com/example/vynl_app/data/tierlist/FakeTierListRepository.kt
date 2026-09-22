package com.example.vynl_app.data.tierlist

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

// In-memory fake, seeded with one tier list for Night Static's albums so the Viewer has
// something to show on first launch, without ever having opened the Builder.
// Item ids are real albumIds (matching FakeAlbumRepository/FakeSearchRepository), not
// ArtistRelease.id — that keeps a tier list item consistent with how every other
// album-referencing type in this app (e.g. CustomList.albumIds) identifies an album.
class FakeTierListRepository : TierListRepository {

    private val tierLists = MutableStateFlow(
        listOf(
            TierList(
                id = "1",
                ownerId = "demo-user",
                artistId = "night-static",
                mode = TierListMode.ALBUMS,
                title = "Night Static — Albums",
                items = listOf(
                    TierListItem("5", TierRank.S), // Static Void Phase — 94% rated
                    TierListItem("2", TierRank.A), // Architectural Echoes — 91%
                    TierListItem("1", TierRank.B)  // Bloom Atlas — 88%
                )
            )
        )
    )

    override suspend fun getTierList(artistId: String, ownerId: String, mode: TierListMode): TierList? {
        delay(300) // fake network delay
        return tierLists.value.find { it.artistId == artistId && it.ownerId == ownerId && it.mode == mode }
    }

    override suspend fun getTierListById(tierListId: String): TierList? {
        delay(300)
        return tierLists.value.find { it.id == tierListId }
    }

    override suspend fun createTierList(
        ownerId: String,
        artistId: String,
        mode: TierListMode,
        title: String,
        itemIds: List<String>
    ): TierList {
        delay(150)
        val newList = TierList(
            id = "local-${System.currentTimeMillis()}",
            ownerId = ownerId,
            artistId = artistId,
            mode = mode,
            title = title,
            items = itemIds.map { TierListItem(it, tier = null) }
        )
        tierLists.value = tierLists.value + newList
        return newList
    }

    override suspend fun setItemTier(tierListId: String, itemId: String, tier: TierRank?) {
        delay(100)
        tierLists.value = tierLists.value.map { list ->
            if (list.id != tierListId) return@map list
            list.copy(items = list.items.map { item -> if (item.itemId == itemId) item.copy(tier = tier) else item })
        }
    }
}
