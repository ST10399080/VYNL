package com.example.vynl_app.ui.tierlistviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.artist.ArtistRepository
import com.example.vynl_app.data.tierlist.TierList
import com.example.vynl_app.data.tierlist.TierListMode
import com.example.vynl_app.data.tierlist.TierListRepository
import com.example.vynl_app.ui.tierlistbuilder.TierListDisplayItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Only depends on the repository INTERFACES, not concrete classes.
// Pass Fake* repositories in for now; swap in the real ones later with zero changes here.
class TierListViewerViewModel(
    private val tierListId: String,
    private val tierListRepository: TierListRepository,
    private val artistRepository: ArtistRepository
) : ViewModel() {

    private val _tierList = MutableStateFlow<TierList?>(null)
    val tierList: StateFlow<TierList?> = _tierList.asStateFlow()

    private val _displayItems = MutableStateFlow<Map<String, TierListDisplayItem>>(emptyMap())
    val displayItems: StateFlow<Map<String, TierListDisplayItem>> = _displayItems.asStateFlow()

    init {
        viewModelScope.launch {
            val list = tierListRepository.getTierListById(tierListId)
            _tierList.value = list
            if (list != null) {
                _displayItems.value = loadCandidates(list.artistId, list.mode).associateBy { it.itemId }
            }
        }
    }

    private suspend fun loadCandidates(artistId: String, mode: TierListMode): List<TierListDisplayItem> = when (mode) {
        TierListMode.ALBUMS -> artistRepository.getEssentialReleases(artistId).map { release ->
            TierListDisplayItem(
                itemId = release.albumId,
                title = release.title,
                subtitle = "${release.year} · ${release.format.name} · ${release.trackCount} tracks"
            )
        }
        TierListMode.SONGS -> artistRepository.getPopularTracks(artistId).map { track ->
            TierListDisplayItem(itemId = track.id, title = track.title, subtitle = track.subtitle)
        }
    }
}
