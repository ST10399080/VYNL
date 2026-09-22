package com.example.vynl_app.ui.listdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.album.AlbumRepository
import com.example.vynl_app.data.list.CustomList
import com.example.vynl_app.data.list.ListRepository
import com.example.vynl_app.data.rating.RatingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Display shape for one row: joins the list's albumIds against AlbumRepository (title,
// artist) and RatingRepository (community score), since CustomList only stores bare ids.
data class ListAlbumEntry(
    val albumId: String,
    val title: String,
    val artist: String,
    val ratingPercentage: Int
)

// Only depends on the repository INTERFACES, not concrete classes.
// Pass Fake* repositories in for now; swap in the real ones later with zero changes here.
class ListDetailViewModel(
    private val listId: String,
    private val listRepository: ListRepository,
    private val albumRepository: AlbumRepository,
    private val ratingRepository: RatingRepository
) : ViewModel() {

    // null until loaded, or if no list with this id exists.
    private val _list = MutableStateFlow<CustomList?>(null)
    val list: StateFlow<CustomList?> = _list.asStateFlow()

    private val _albums = MutableStateFlow<List<ListAlbumEntry>>(emptyList())
    val albums: StateFlow<List<ListAlbumEntry>> = _albums.asStateFlow()

    init {
        loadList()
    }

    private fun loadList() {
        viewModelScope.launch {
            val loadedList = listRepository.getListById(listId)
            _list.value = loadedList
            _albums.value = loadedList?.albumIds.orEmpty().map { albumId ->
                val detail = albumRepository.getAlbumDetail(albumId)
                val summary = ratingRepository.getAlbumRatingSummary(albumId)
                ListAlbumEntry(albumId, detail.title, detail.artist, summary.percentage)
            }
        }
    }

    // Called from an album row's entry-options menu.
    fun onAlbumRemoved(albumId: String) {
        viewModelScope.launch {
            listRepository.setAlbumMembership(listId, albumId, isMember = false)
            loadList()
        }
    }
}
