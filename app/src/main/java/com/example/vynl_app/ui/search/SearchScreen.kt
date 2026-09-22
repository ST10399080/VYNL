package com.example.vynl_app.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onAlbumClick: (String) -> Unit = {},
    onArtistClick: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    val currentTab by viewModel.currentTab.collectAsState()
    val albumResults by viewModel.albumResults.collectAsState()
    val songResults by viewModel.songResults.collectAsState()
    val artistResults by viewModel.artistResults.collectAsState()

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.onQueryChanged(it) // triggers the debounced search, not an immediate one
            },
            label = { Text("Search albums, songs, artists...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        SearchTabRow(selectedTab = currentTab, onTabSelected = viewModel::onTabSelected)

        when (currentTab) {
            SearchTab.ALBUMS -> LazyColumn {
                items(albumResults) { album -> AlbumResultRow(album, onClick = { onAlbumClick(album.id) }) }
            }
            SearchTab.SONGS -> LazyColumn {
                items(songResults) { song -> SongResultRow(song, onClick = { /* no Song Detail screen yet */ }) }
            }
            SearchTab.ARTISTS -> LazyColumn {
                items(artistResults) { artist -> ArtistResultRow(artist, onClick = { onArtistClick(artist.id) }) }
            }
            SearchTab.USERS -> UsersComingSoonEmptyState()
        }
    }
}
