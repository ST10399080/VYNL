package com.example.vynl_app.data.list

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

// In-memory fake, no network at all. "Late Night Drives" already contains "Bloom Atlas"
// (albumId "1") so the Add to List sheet's pre-checked checkbox state is demonstrable.
class FakeListRepository : ListRepository {

    private val lists = MutableStateFlow(
        listOf(
            CustomList(
                id = "1",
                ownerId = "demo-user",
                title = "Late Night Drives",
                description = "Slow-burn atmosphere for the road after midnight.",
                albumIds = listOf("1"),
                isPublic = true
            ),
            CustomList(
                id = "2",
                ownerId = "demo-user",
                title = "Certified Bangers",
                description = "No skips.",
                albumIds = emptyList(),
                isPublic = true
            ),
            CustomList(
                id = "3",
                ownerId = "demo-user",
                title = "Deep Cuts",
                description = "Overlooked, underrated.",
                albumIds = emptyList(),
                isPublic = false
            )
        )
    )

    override suspend fun getListsForUser(userId: String): List<CustomList> {
        delay(300) // fake network delay
        return lists.value.filter { it.ownerId == userId }
    }

    override suspend fun getListById(listId: String): CustomList? {
        delay(300)
        return lists.value.find { it.id == listId }
    }

    override suspend fun setAlbumMembership(listId: String, albumId: String, isMember: Boolean) {
        delay(150)
        lists.value = lists.value.map { list ->
            if (list.id != listId) return@map list
            val updatedAlbumIds = if (isMember) list.albumIds + albumId else list.albumIds - albumId
            list.copy(albumIds = updatedAlbumIds)
        }
    }

    override suspend fun createList(userId: String, title: String): CustomList {
        delay(150)
        val newList = CustomList(
            id = "local-${System.currentTimeMillis()}",
            ownerId = userId,
            title = title,
            description = "",
            albumIds = emptyList(),
            isPublic = true
        )
        lists.value = lists.value + newList
        return newList
    }
}
