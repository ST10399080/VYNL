package com.example.vynl_app.ui.homefeed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.ui.theme.VynlColors

@Composable
fun HomeFeedScreen(
    viewModel: HomeFeedViewModel,
    onSearchClick: () -> Unit = {},
    // TODO: notifications screen (Figure 7 nav diagram, "Notifications") — not built yet.
    onNotificationsClick: () -> Unit = {},
    onAlbumClick: (String) -> Unit = {},
    onArtistClick: (String) -> Unit = {},
    onMyListsClick: () -> Unit = {}
) {
    val hero by viewModel.hero.collectAsState()
    val trending by viewModel.trending.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(VynlColors.Background)
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(56.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "VYNL",
                modifier = Modifier.align(Alignment.CenterStart),
                color = VynlColors.TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Row(Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = onSearchClick, modifier = Modifier.alpha(0.8f)) {
                    Icon(Icons.Filled.Search, contentDescription = "Search", tint = VynlColors.TextPrimary, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onNotificationsClick, modifier = Modifier.alpha(0.8f)) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = VynlColors.TextPrimary, modifier = Modifier.size(18.dp))
                }
                Box {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.alpha(0.8f)) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options", tint = VynlColors.TextPrimary, modifier = Modifier.size(18.dp))
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("My Lists") },
                            onClick = {
                                showMenu = false
                                onMyListsClick()
                            }
                        )
                    }
                }
            }
        }

        if (hero == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VynlColors.Accent)
            }
        } else {
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                HeroCard(
                    album = hero!!,
                    onAlbumClick = { onAlbumClick(hero!!.albumId) },
                    onArtistClick = { onArtistClick(hero!!.artistId) }
                )

                Text("Top", color = VynlColors.TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)

                Column {
                    trending.forEachIndexed { index, album ->
                        FeaturedAlbumRow(
                            album = album,
                            rank = index + 2, // hero already occupies #1
                            onAlbumClick = { onAlbumClick(album.albumId) },
                            onArtistClick = { onArtistClick(album.artistId) }
                        )
                    }
                }
            }
        }
    }
}
