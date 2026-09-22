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
    // TODO: share artist profile. The design doc only specifies shareable links for
    // custom lists (section 9, "Shareable list links"); no equivalent is described for
    // artist profiles. Would need its own deep-link scheme before this can do anything.
    onShare: () -> Unit = {},
    // TODO: overflow menu. Contents aren't specified anywhere — the Figma frame (1:964)
    // never expands this menu, and the design doc doesn't mention one for Artist Detail.
    onMoreOptions: () -> Unit = {},
    // TODO: follow system (design doc section 9, "Follow system"). Following an artist
    // isn't explicitly covered — that section is about following other users — but the
    // same underlying /follows resource (REST API Endpoints table) is the likely fit.
    // Drives the activity feed and "followed artist releases a new album" notifications
    // (section 5) once wired up.
    onFollowClick: () -> Unit = {},
    // TODO: play radio. No backend endpoint exists for this anywhere in the design doc's
    // REST API Endpoints table, and no playback engine exists in the app yet either.
    onPlayRadioClick: () -> Unit = {},
    // TODO: opens the "Immersive Artist Story View" screen (Figure 7 nav diagram) — not
    // built yet.
    onStoriesClick: () -> Unit = {},
    onReadFullBioClick: () -> Unit = {},
    onReleaseClick: (String) -> Unit = {},
    onTrackAlbumClick: (String) -> Unit = {},
    onRankAlbumsClick: () -> Unit = {},
    onRankSongsClick: () -> Unit = {}
) {
    val artist by viewModel.artist.collectAsState()
    val essentialReleases by viewModel.essentialReleases.collectAsState()
    val popularTracks by viewModel.popularTracks.collectAsState()

    Column(Modifier.fillMaxSize()) {
        ArtistDetailTopBar(
            onBack = onBack,
            onShare = onShare,
            onMoreOptions = onMoreOptions,
            onRankAlbumsClick = onRankAlbumsClick,
            onRankSongsClick = onRankSongsClick
        )

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
                    onTrackAlbumClick = onTrackAlbumClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                PatchArchitectureNote(modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
    }
}
