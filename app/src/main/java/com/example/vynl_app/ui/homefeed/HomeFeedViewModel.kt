package com.example.vynl_app.ui.homefeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vynl_app.data.home.FeaturedAlbum
import com.example.vynl_app.data.home.HomeFeedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Only depends on the repository INTERFACE, not a concrete class.
// Pass FakeHomeFeedRepository in for now; swap in the real one later with zero changes here.
class HomeFeedViewModel(private val homeFeedRepository: HomeFeedRepository) : ViewModel() {

    // null until loaded; the screen shows a spinner in the meantime.
    private val _hero = MutableStateFlow<FeaturedAlbum?>(null)
    val hero: StateFlow<FeaturedAlbum?> = _hero.asStateFlow()

    private val _trending = MutableStateFlow<List<FeaturedAlbum>>(emptyList())
    val trending: StateFlow<List<FeaturedAlbum>> = _trending.asStateFlow()

    init {
        viewModelScope.launch {
            _hero.value = homeFeedRepository.getHeroAlbum()
            _trending.value = homeFeedRepository.getTrendingAlbums()
        }
    }
}
