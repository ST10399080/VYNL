package com.example.vynl_app.data.tierlist

// Contract for per-artist tier lists. Real implementation will read from Firestore.
// UI only ever depends on this interface.
interface TierListRepository {
    // Null if this user has never built a tier list for this artist in this mode.
    suspend fun getTierList(artistId: String, ownerId: String, mode: TierListMode): TierList?

    suspend fun getTierListById(tierListId: String): TierList?

    // Creates a fresh, fully-unranked tier list. Called on first open of the Builder.
    suspend fun createTierList(ownerId: String, artistId: String, mode: TierListMode, title: String, itemIds: List<String>): TierList

    // Places, moves, or un-ranks (tier = null) one item. Auto-saves on every tap.
    suspend fun setItemTier(tierListId: String, itemId: String, tier: TierRank?)
}
