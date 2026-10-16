package com.example.vynl_app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.album.Album
import com.example.vynl_app.data.album.SearchRepository
import com.example.vynl_app.data.artist.ArtistSearchRepository
import com.example.vynl_app.data.artist.ArtistSummary
import com.example.vynl_app.data.song.Song
import com.example.vynl_app.data.song.SongSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

enum class SearchTab { ALBUMS, SONGS, ARTISTS, USERS }

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val albumRepository: SearchRepository,
    private val songRepository: SongSearchRepository,
    private val artistRepository: ArtistSearchRepository
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val _currentTab = MutableStateFlow(SearchTab.ALBUMS)
    val currentTab: StateFlow<SearchTab> = _currentTab.asStateFlow()

    // Shared debounced query. Combined with the active tab so switching tabs only
    // re-triggers the repository call for whichever tab is now showing, not all four.
    private val debouncedQuery = query
        .debounce(400)
        .distinctUntilChanged()

    val albumResults: StateFlow<List<Album>> = debouncedQuery
        .combine(_currentTab) { text, tab -> text to tab }
        .filter { (_, tab) -> tab == SearchTab.ALBUMS }
        .flatMapLatest { (text, _) -> flow { emit(albumRepository.searchAlbums(text)) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val songResults: StateFlow<List<Song>> = debouncedQuery
        .combine(_currentTab) { text, tab -> text to tab }
        .filter { (_, tab) -> tab == SearchTab.SONGS }
        .flatMapLatest { (text, _) -> flow { emit(songRepository.searchSongs(text)) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val artistResults: StateFlow<List<ArtistSummary>> = debouncedQuery
        .combine(_currentTab) { text, tab -> text to tab }
        .filter { (_, tab) -> tab == SearchTab.ARTISTS }
        .flatMapLatest { (text, _) -> flow { emit(artistRepository.searchArtists(text)) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChanged(text: String) {
        query.value = text
    }

    fun onTabSelected(tab: SearchTab) {
        _currentTab.value = tab
    }
}
