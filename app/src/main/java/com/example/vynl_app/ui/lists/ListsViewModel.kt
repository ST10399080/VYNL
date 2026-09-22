package com.example.vynl_app.ui.lists

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
class ListsViewModel(
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

    // Called from "+ Create List" on the index screen.
    fun onListCreated(title: String) {
        viewModelScope.launch {
            listRepository.createList(currentUserId, title)
            loadLists()
        }
    }
}
