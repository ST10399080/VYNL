package com.example.vynl_app.ui.artistdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vynl_app.ui.theme.VynlColors

@Composable
fun ArtistDetailScreen(
    viewModel: ArtistDetailViewModel,
    onBack: () -> Unit = {},
    onShare: () -> Unit = {},
    onMoreOptions: () -> Unit = {},
    onFollowClick: () -> Unit = {},
    onPlayRadioClick: () -> Unit = {},
    onStoriesClick: () -> Unit = {},
    onReadFullBioClick: () -> Unit = {},
    onReleaseClick: (String) -> Unit = {}
) {
    val artist by viewModel.artist.collectAsState()
    val essentialReleases by viewModel.essentialReleases.collectAsState()
    val popularTracks by viewModel.popularTracks.collectAsState()

    Column(Modifier.fillMaxSize()) {
        ArtistDetailTopBar(onBack = onBack, onShare = onShare, onMoreOptions = onMoreOptions)

        val detail = artist
        if (detail == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VynlColors.Accent)
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                HeroSection(
                    artist = detail,
                    onFollowClick = onFollowClick,
                    onPlayRadioClick = onPlayRadioClick,
                    onStoriesClick = onStoriesClick
                )
                MetricsRow(artist = detail, modifier = Modifier.padding(horizontal = 20.dp))
                BiographySection(
                    biography = detail.biography,
                    onReadFullBioClick = onReadFullBioClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                EssentialReleasesSection(
                    releases = essentialReleases,
                    onReleaseClick = onReleaseClick,
                    modifier = Modifier.padding(start = 20.dp)
                )
                PopularTracksSection(
                    tracks = popularTracks,
                    onTrackPlayClick = { /* playback isn't built yet */ },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                PatchArchitectureNote(modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
    }
}
