package com.example.vynl_app.ui.tierlistbuilder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.artist.ArtistRepository
import com.example.vynl_app.data.tierlist.TierList
import com.example.vynl_app.data.tierlist.TierListMode
import com.example.vynl_app.data.tierlist.TierListRepository
import com.example.vynl_app.data.tierlist.TierRank
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Mode-agnostic display shape so the Builder/Viewer UI never needs to know whether it's
// looking at an ArtistRelease or an ArtistTrack.
data class TierListDisplayItem(
    val itemId: String,
    val title: String,
    val subtitle: String
)

// Only depends on the repository INTERFACES, not concrete classes.
// Pass Fake* repositories in for now; swap in the real ones later with zero changes here.
class TierListBuilderViewModel(
    private val artistId: String,
    private val mode: TierListMode,
    private val currentUserId: String,
    private val tierListRepository: TierListRepository,
    private val artistRepository: ArtistRepository
) : ViewModel() {

    private val _tierList = MutableStateFlow<TierList?>(null)
    val tierList: StateFlow<TierList?> = _tierList.asStateFlow()

    private val _displayItems = MutableStateFlow<Map<String, TierListDisplayItem>>(emptyMap())
    val displayItems: StateFlow<Map<String, TierListDisplayItem>> = _displayItems.asStateFlow()

    // Nothing selected until the user taps a card. Drives the tap-to-assign flow.
    private val _selectedItemId = MutableStateFlow<String?>(null)
    val selectedItemId: StateFlow<String?> = _selectedItemId.asStateFlow()

    init {
        viewModelScope.launch {
            val candidates = loadCandidates()
            _displayItems.value = candidates.associateBy { it.itemId }

            // Load an existing tier list for this artist/mode, or create a fresh
            // fully-unranked one on first open — no separate "create" step.
            _tierList.value = tierListRepository.getTierList(artistId, currentUserId, mode)
                ?: tierListRepository.createTierList(
                    ownerId = currentUserId,
                    artistId = artistId,
                    mode = mode,
                    title = defaultTitle(),
                    itemIds = candidates.map { it.itemId }
                )
        }
    }

    private suspend fun loadCandidates(): List<TierListDisplayItem> = when (mode) {
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

    private suspend fun defaultTitle(): String {
        val name = artistRepository.getArtistDetail(artistId).name
        val label = if (mode == TierListMode.ALBUMS) "Albums" else "Songs"
        return "$name — $label"
    }

    private fun reload() {
        viewModelScope.launch {
            _tierList.value = tierListRepository.getTierList(artistId, currentUserId, mode)
        }
    }

    // Tapping a card in the tray or a tier: same item tapped again deselects, a different
    // item switches selection, nothing happens if nothing was selected.
    fun onItemTapped(itemId: String) {
        _selectedItemId.value = if (_selectedItemId.value == itemId) null else itemId
    }

    // Tapping a tier row. No-op if nothing is selected. Tapping the tier the selected item
    // is already in is treated as a cancel rather than a no-op move.
    fun onTierTapped(tier: TierRank) {
        val selected = _selectedItemId.value ?: return
        val list = _tierList.value ?: return
        val currentTier = list.items.find { it.itemId == selected }?.tier
        if (currentTier == tier) {
            _selectedItemId.value = null
            return
        }
        viewModelScope.launch {
            tierListRepository.setItemTier(list.id, selected, tier)
            _selectedItemId.value = null
            reload()
        }
    }

    // "Move to Unranked" action, shown only while a placed item is selected.
    fun onMoveToUnrankedTapped() {
        val selected = _selectedItemId.value ?: return
        val list = _tierList.value ?: return
        viewModelScope.launch {
            tierListRepository.setItemTier(list.id, selected, null)
            _selectedItemId.value = null
            reload()
        }
    }

    // "Cancel" action — backs out of a selection without changing anything.
    fun onCancelSelection() {
        _selectedItemId.value = null
    }
}
