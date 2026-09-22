package com.example.vynl_app.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.list.CustomList
import com.example.vynl_app.data.list.ListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Only depends on the repository INTERFACE, not a concrete class.
// Pass FakeListRepository in for now; swap in the real one later with zero changes here.
class AddToListViewModel(
    private val currentUserId: String,
    private val listRepository: ListRepository
) : ViewModel() {

    private val _lists = MutableStateFlow<List<CustomList>>(emptyList())
    val lists: StateFlow<List<CustomList>> = _lists.asStateFlow()

    init {
        loadLists()
    }

    private fun loadLists() {
        viewModelScope.launch {
            _lists.value = listRepository.getListsForUser(currentUserId)
        }
    }

    // Called when the user taps a list's checkbox.
    fun onMembershipToggled(listId: String, albumId: String, isMember: Boolean) {
        viewModelScope.launch {
            listRepository.setAlbumMembership(listId, albumId, isMember)
            loadLists() // refresh so the checkbox state reflects the change
        }
    }

    // Called when the user creates a new list, then immediately adds the current album to it.
    fun onListCreated(title: String, albumId: String) {
        viewModelScope.launch {
            val newList = listRepository.createList(currentUserId, title)
            listRepository.setAlbumMembership(newList.id, albumId, isMember = true)
            loadLists()
        }
    }
}
