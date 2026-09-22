package com.example.vynl_app.ui.artistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.artist.ArtistDetail
import com.example.vynl_app.data.artist.ArtistRelease
import com.example.vynl_app.data.artist.ArtistRepository
import com.example.vynl_app.data.artist.ArtistTrack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Only depends on the repository INTERFACE, not a concrete class.
// Pass FakeArtistRepository in for now; swap in the real one later with zero changes here.
class ArtistDetailViewModel(
    private val artistId: String,
    private val artistRepository: ArtistRepository
) : ViewModel() {

    // null until the artist has loaded; the screen shows a spinner in the meantime.
    private val _artist = MutableStateFlow<ArtistDetail?>(null)
    val artist: StateFlow<ArtistDetail?> = _artist.asStateFlow()

    private val _essentialReleases = MutableStateFlow<List<ArtistRelease>>(emptyList())
    val essentialReleases: StateFlow<List<ArtistRelease>> = _essentialReleases.asStateFlow()

    private val _popularTracks = MutableStateFlow<List<ArtistTrack>>(emptyList())
    val popularTracks: StateFlow<List<ArtistTrack>> = _popularTracks.asStateFlow()

    init {
        loadArtist()
        loadEssentialReleases()
        loadPopularTracks()
    }

    private fun loadArtist() {
        viewModelScope.launch {
            _artist.value = artistRepository.getArtistDetail(artistId)
        }
    }

    private fun loadEssentialReleases() {
        viewModelScope.launch {
            _essentialReleases.value = artistRepository.getEssentialReleases(artistId)
        }
    }

    private fun loadPopularTracks() {
        viewModelScope.launch {
            _popularTracks.value = artistRepository.getPopularTracks(artistId)
        }
    }
}
