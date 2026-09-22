package com.example.vynl_app.data.list

// Contract for a user's custom lists. Real implementation will read from Firestore.
// UI only ever depends on this interface.
interface ListRepository {
    suspend fun getListsForUser(userId: String): List<CustomList>

    // Null if no list with this id exists. Used by List Detail.
    suspend fun getListById(listId: String): CustomList?

    // Toggles whether albumId is in listId. Called from the Add to List sheet's checkboxes.
    suspend fun setAlbumMembership(listId: String, albumId: String, isMember: Boolean)

    // Creates a new empty list and returns it, so the caller can immediately add the
    // current album to it.
    suspend fun createList(userId: String, title: String): CustomList
}
