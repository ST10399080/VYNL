package com.example.vynl_app.ui.artistdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.data.artist.ArtistDetail
import com.example.vynl_app.data.artist.ArtistRelease
import com.example.vynl_app.data.artist.ArtistTrack
import com.example.vynl_app.data.artist.ReleaseFormat
import com.example.vynl_app.ui.theme.VynlColors

// Text styles from the Figma frame (default font for now; Inter/Liberation Mono are deferred).
private object ArtistText {
    val Heading = TextStyle(fontSize = 24.sp, lineHeight = 25.92.sp, letterSpacing = (-0.6).sp, fontWeight = FontWeight.Medium)
    val HeadingSmall = TextStyle(fontSize = 18.sp, lineHeight = 27.sp, letterSpacing = (-0.54).sp, fontWeight = FontWeight.Medium)
    val Body = TextStyle(fontSize = 14.sp, lineHeight = 17.5.sp, letterSpacing = (-0.42).sp)
    val BodyMedium = Body.copy(fontWeight = FontWeight.Medium)
    val Caption = TextStyle(fontSize = 12.sp, lineHeight = 14.sp, letterSpacing = (-0.396).sp)
    val Label = TextStyle(fontSize = 12.sp, lineHeight = 14.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Medium)
    val Tag = TextStyle(fontSize = 10.sp, lineHeight = 12.9.sp, letterSpacing = 0.5.sp)
}

private val CardShape = RoundedCornerShape(10.dp)

private fun formatCompactCount(count: Long): String = when {
    count >= 1_000_000 -> "%.1fM".format(count / 1_000_000.0).replace(".0M", "M")
    count >= 1_000 -> "%.0fk".format(count / 1_000.0)
    else -> count.toString()
}

@Composable
fun ArtistDetailTopBar(
    onBack: () -> Unit,
    onShare: () -> Unit,
    onMoreOptions: () -> Unit,
    onRankAlbumsClick: () -> Unit = {},
    onRankSongsClick: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .background(VynlColors.Background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 12.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart).alpha(0.8f)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = VynlColors.TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                "ARTIST PROFILE",
                modifier = Modifier.align(Alignment.Center),
                color = VynlColors.TextSecondary,
                style = ArtistText.Label
            )
            Row(modifier = Modifier.align(Alignment.CenterEnd), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onShare, modifier = Modifier.alpha(0.8f)) {
                    Icon(Icons.Filled.Share, contentDescription = "Share artist", tint = VynlColors.TextPrimary, modifier = Modifier.size(16.dp))
                }
                Box {
                    IconButton(
                        onClick = {
                            showMenu = true
                            onMoreOptions()
                        },
                        modifier = Modifier.alpha(0.8f)
                    ) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options", tint = VynlColors.TextPrimary, modifier = Modifier.size(16.dp))
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Rank Albums") },
                            onClick = {
                                showMenu = false
                                onRankAlbumsClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Rank Songs") },
                            onClick = {
                                showMenu = false
                                onRankSongsClick()
                            }
                        )
                    }
                }
            }
        }
    }
}

// Placeholder until an image loader (Coil) is added.
// Swap point: replace this Box with AsyncImage(model = <heroUrl / coverUrl>, contentScale = ContentScale.Crop, ...)
@Composable
private fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier.background(
            Brush.linearGradient(listOf(VynlColors.Accent.copy(alpha = 0.55f), VynlColors.SurfaceVariant))
        )
    )
}

@Composable
fun HeroSection(
    artist: ArtistDetail,
    onFollowClick: () -> Unit,
    onPlayRadioClick: () -> Unit,
    onStoriesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxWidth().aspectRatio(4f / 3f)) {
        ImagePlaceholder(Modifier.fillMaxSize())
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(VynlColors.Background.copy(alpha = 0.6f), VynlColors.Background.copy(alpha = 0f))))
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(VynlColors.Background.copy(alpha = 0f), VynlColors.Background)))
        )

        if (artist.isVerified) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(VynlColors.Background.copy(alpha = 0.6f), RoundedCornerShape(7.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = VynlColors.Accent, modifier = Modifier.size(12.dp))
                Text("Verified Artist", color = VynlColors.TextPrimary, style = ArtistText.Caption)
            }
        }

        Column(
            Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column {
                Text(artist.name, color = VynlColors.TextPrimary, style = ArtistText.Heading)
                Text(artist.tagline, color = VynlColors.Accent, style = ArtistText.Body)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = onFollowClick,
                    shape = CardShape,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = VynlColors.SurfaceVariant,
                        contentColor = VynlColors.TextPrimary
                    )
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Follow", style = ArtistText.Body)
                }
                OutlinedButton(
                    onClick = onPlayRadioClick,
                    shape = CardShape,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = VynlColors.TextPrimary,
                        contentColor = VynlColors.OnAccent
                    )
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Play Radio", style = ArtistText.Body)
                }
                IconButton(
                    onClick = onStoriesClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(VynlColors.SurfaceVariant, CardShape)
                ) {
                    Icon(Icons.Filled.Face, contentDescription = "Artist stories", tint = VynlColors.TextPrimary, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun MetricsRow(artist: ArtistDetail, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(VynlColors.Surface, CardShape)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Metric(value = formatCompactCount(artist.monthlyListeners), label = "Monthly Listeners", modifier = Modifier.weight(1f))
        Metric(value = formatCompactCount(artist.followers), label = "Followers", modifier = Modifier.weight(1f))
        Metric(value = "%.1f".format(artist.vynlIndex), label = "Vynl Index", showAccentDot = true, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun Metric(value: String, label: String, modifier: Modifier = Modifier, showAccentDot: Boolean = false) {
    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(value, color = VynlColors.TextPrimary, style = ArtistText.HeadingSmall)
            if (showAccentDot) {
                Box(Modifier.size(6.dp).background(VynlColors.Accent, CircleShape))
            }
        }
        Spacer(Modifier.height(2.dp))
        Text(label.uppercase(), color = VynlColors.TextSecondary, style = ArtistText.Caption)
    }
}

@Composable
fun BiographySection(biography: String, onReadFullBioClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("BIOGRAPHY", color = VynlColors.TextSecondary, style = ArtistText.Label)
        Text(biography, color = VynlColors.TextSecondary, style = ArtistText.Body)
        Row(
            modifier = Modifier.clickable { onReadFullBioClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Read full bio", color = VynlColors.Accent, style = ArtistText.Body)
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = VynlColors.Accent, modifier = Modifier.size(10.dp))
        }
    }
}

@Composable
fun EssentialReleasesSection(
    releases: List<ArtistRelease>,
    onReleaseClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Essential Releases", color = VynlColors.TextPrimary, style = ArtistText.HeadingSmall)
            Text("View All (${releases.size})", color = VynlColors.TextSecondary, style = ArtistText.Caption)
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(releases) { release -> ReleaseCard(release, onClick = { onReleaseClick(release.albumId) }) }
        }
    }
}

@Composable
private fun ReleaseCard(release: ArtistRelease, onClick: () -> Unit) {
    Column(
        Modifier
            .width(176.dp)
            .clickable(onClick = onClick)
            .background(VynlColors.Surface, CardShape)
            .clip(CardShape)
    ) {
        Box(Modifier.fillMaxWidth().aspectRatio(1f)) {
            ImagePlaceholder(Modifier.fillMaxSize())
            Text(
                release.format.name,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .background(VynlColors.Background.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                color = VynlColors.TextPrimary,
                style = ArtistText.Tag
            )
        }
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(release.title, color = VynlColors.TextPrimary, style = ArtistText.BodyMedium)
            Text(
                "${release.year} · ${release.format.name} · ${release.trackCount} tracks",
                color = VynlColors.TextSecondary,
                style = ArtistText.Caption
            )
            Text(
                "${release.ratingPercentage}% Rating · ${formatCompactCount(release.ratingCount)}",
                color = VynlColors.Accent,
                style = ArtistText.Caption
            )
        }
    }
}

@Composable
fun PopularTracksSection(
    tracks: List<ArtistTrack>,
    onTrackPlayClick: (ArtistTrack) -> Unit,
    onTrackAlbumClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Popular Tracks", color = VynlColors.TextPrimary, style = ArtistText.HeadingSmall)
            Text("Last 30 Days", color = VynlColors.TextSecondary, style = ArtistText.Caption)
        }
        Column(Modifier.fillMaxWidth().background(VynlColors.Surface, CardShape).clip(CardShape)) {
            tracks.forEachIndexed { index, track ->
                if (index > 0) HorizontalDivider(color = VynlColors.BorderMuted)
                PopularTrackRow(
                    track,
                    onPlayClick = { onTrackPlayClick(track) },
                    // Only navigates for a track that belongs to an album; a standalone
                    // single (albumId == null) has nowhere to go, so the row tap is a no-op.
                    onRowClick = track.albumId?.let { albumId -> { onTrackAlbumClick(albumId) } } ?: {}
                )
            }
        }
    }
}

@Composable
private fun PopularTrackRow(track: ArtistTrack, onPlayClick: () -> Unit, onRowClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onRowClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "%02d".format(track.rank),
                color = VynlColors.TextSecondary,
                style = ArtistText.Caption,
                modifier = Modifier.width(20.dp)
            )
            IconButton(
                onClick = onPlayClick,
                modifier = Modifier.size(32.dp).background(VynlColors.SurfaceVariant, CircleShape)
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Play ${track.title}", tint = VynlColors.TextPrimary, modifier = Modifier.size(14.dp))
            }
            Column {
                Text(track.title, color = VynlColors.TextPrimary, style = ArtistText.BodyMedium)
                Text(track.subtitle, color = VynlColors.TextSecondary, style = ArtistText.Caption)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(formatCompactCount(track.playCount), color = VynlColors.TextSecondary, style = ArtistText.Caption)
            Icon(Icons.Filled.MoreVert, contentDescription = "Track actions", tint = VynlColors.TextSecondary, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun PatchArchitectureNote(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .background(VynlColors.SurfaceMuted, CardShape)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.size(36.dp).background(VynlColors.SurfaceVariant, CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.Settings, contentDescription = null, tint = VynlColors.TextPrimary, modifier = Modifier.size(14.dp))
        }
        Column(Modifier.weight(1f)) {
            Text("Patch Architecture", color = VynlColors.TextPrimary, style = ArtistText.Caption.copy(fontWeight = FontWeight.Medium))
            Text(
                "Eurorack Dual VCO · Buchla 258 Format · Custom Logic",
                color = VynlColors.TextSecondary,
                style = ArtistText.Caption,
                maxLines = 1
            )
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = VynlColors.TextSecondary, modifier = Modifier.size(12.dp))
    }
}
