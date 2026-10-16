package com.example.vynl_app.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vynl_app.data.album.Album
import com.example.vynl_app.data.artist.ArtistSummary
import com.example.vynl_app.data.song.Song
import com.example.vynl_app.ui.theme.VynlColors

private fun SearchTab.label(): String = when (this) {
    SearchTab.ALBUMS -> "Albums"
    SearchTab.SONGS -> "Songs"
    SearchTab.ARTISTS -> "Artists"
    SearchTab.USERS -> "Users"
}

@Composable
fun SearchTabRow(selectedTab: SearchTab, onTabSelected: (SearchTab) -> Unit) {
    TabRow(selectedTabIndex = selectedTab.ordinal, containerColor = VynlColors.Background) {
        SearchTab.entries.forEach { tab ->
            Tab(
                selected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                text = { Text(tab.label()) }
            )
        }
    }
}

@Composable
fun AlbumResultRow(album: Album, onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text("${album.title} — ${album.artist}", color = VynlColors.TextPrimary)
    }
}

@Composable
fun SongResultRow(song: Song, onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text("${song.title} — ${song.artist}", color = VynlColors.TextPrimary)
        Text(song.album, color = VynlColors.TextSecondary)
    }
}

@Composable
fun ArtistResultRow(artist: ArtistSummary, onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(artist.name, color = VynlColors.TextPrimary)
        Text(artist.tagline, color = VynlColors.TextSecondary)
    }
}

@Composable
fun UsersComingSoonEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("User search coming soon", color = VynlColors.TextSecondary)
    }
}
