package com.example.vynl_app.ui.albumdetail

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.data.album.AlbumDetail
import com.example.vynl_app.data.album.Track
import com.example.vynl_app.data.review.Review
import com.example.vynl_app.ui.theme.VynlColors

// Text styles from the Figma frame (default font for now; Inter is deferred).
private object AlbumText {
    val Heading = TextStyle(fontSize = 24.sp, lineHeight = 25.92.sp, letterSpacing = (-0.6).sp, fontWeight = FontWeight.Medium)
    val Body = TextStyle(fontSize = 14.sp, lineHeight = 17.5.sp, letterSpacing = (-0.42).sp)
    val BodyMedium = Body.copy(fontWeight = FontWeight.Medium)
    val Caption = TextStyle(fontSize = 12.sp, lineHeight = 14.sp, letterSpacing = (-0.396).sp)
    val Button = TextStyle(fontSize = 16.sp, lineHeight = 20.64.sp, letterSpacing = (-0.48).sp, fontWeight = FontWeight.Medium)
}

private val CardShape = RoundedCornerShape(8.dp)

@Composable
fun AlbumDetailTopBar(
    onBack: () -> Unit,
    onSearchClick: () -> Unit,
    onAddToListClick: () -> Unit = {},
    onMyListsClick: () -> Unit = {}
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
                .height(64.dp)
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
                "VYNL",
                modifier = Modifier.align(Alignment.Center),
                color = VynlColors.TextPrimary,
                style = AlbumText.Heading.copy(lineHeight = 26.4.sp, letterSpacing = (-1.2).sp)
            )
            Row(modifier = Modifier.align(Alignment.CenterEnd), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onSearchClick, modifier = Modifier.alpha(0.8f)) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = VynlColors.TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Box {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.alpha(0.8f)) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "More options",
                            tint = VynlColors.TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Add to List") },
                            onClick = {
                                showMenu = false
                                onAddToListClick()
                            }
                        )
                        // Temporary placement until Profile exists — per the design doc's
                        // Figure 7 nav diagram, Lists is reached from Profile, not here.
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
        HorizontalDivider(color = VynlColors.BorderMuted)
    }
}

// Placeholder until an image loader (Coil) is added.
// Swap point: replace this Box with AsyncImage(model = <coverUrl / avatarUrl>, contentScale = ContentScale.Crop, ...)
@Composable
private fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier.background(
            Brush.linearGradient(listOf(VynlColors.Accent.copy(alpha = 0.55f), VynlColors.SurfaceVariant))
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumHeader(
    album: AlbumDetail,
    rateLabel: String,
    onRateClick: () -> Unit,
    onPlayClick: () -> Unit,
    onArtistClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(140.dp)
                .background(VynlColors.Surface, CardShape)
                .border(1.dp, VynlColors.BorderMuted, CardShape)
                .padding(1.dp)
                .clip(CardShape)
        ) {
            // Swap point for AsyncImage(model = album.coverUrl)
            ImagePlaceholder(Modifier.fillMaxSize())
        }

        Spacer(Modifier.height(20.dp))
        Text(album.title, color = VynlColors.TextPrimary, style = AlbumText.Heading, textAlign = TextAlign.Center)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                album.artist,
                color = VynlColors.Accent,
                style = AlbumText.Body,
                modifier = Modifier.clickable(onClick = onArtistClick)
            )
            Text(
                "•",
                modifier = Modifier.padding(start = 8.dp),
                color = VynlColors.TextSecondary,
                style = AlbumText.Button.copy(fontWeight = FontWeight.Normal)
            )
            Text(
                "${album.year} · ${album.tracks.size} tracks",
                modifier = Modifier.padding(start = 8.dp),
                color = VynlColors.TextSecondary,
                style = AlbumText.Caption
            )
        }

        Spacer(Modifier.height(20.dp))
        Row(
            Modifier.widthIn(max = 320.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onRateClick,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = CardShape,
                border = BorderStroke(1.dp, VynlColors.TextPrimary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VynlColors.TextPrimary)
            ) {
                Text(rateLabel, style = AlbumText.Button)
            }
            Spacer(Modifier.width(8.dp))
            OutlinedIconButton(
                onClick = onPlayClick,
                modifier = Modifier.size(48.dp),
                shape = CardShape,
                border = BorderStroke(1.dp, VynlColors.TextPrimary),
                colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = VynlColors.TextPrimary)
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Play", modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun StatsSection(
    communityPercentage: Int,
    criticScore: Int?,
    isCommunityFavorite: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (isCommunityFavorite) {
            Text(
                "COMMUNITY FAVORITE",
                modifier = Modifier
                    .border(1.dp, VynlColors.TextPrimary.copy(alpha = 0.4f), RoundedCornerShape(7.dp))
                    .padding(horizontal = 13.dp, vertical = 5.dp),
                color = VynlColors.TextPrimary,
                style = AlbumText.Caption.copy(letterSpacing = 0.6.sp)
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(VynlColors.Surface, CardShape)
                .border(1.dp, VynlColors.BorderMuted, CardShape)
        ) {
            Stat(
                label = "Community Score",
                value = "$communityPercentage%",
                valueColor = VynlColors.TextPrimary,
                modifier = Modifier.weight(1f)
            )
            VerticalDivider(color = VynlColors.BorderMuted)
            Stat(
                label = "Critic Score",
                value = criticScore?.let { "$it%" } ?: "Pending",
                valueColor = if (criticScore != null) VynlColors.TextPrimary else VynlColors.TextSecondary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Stat(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Text(label, color = VynlColors.TextSecondary, style = AlbumText.Caption)
        Spacer(Modifier.height(4.dp))
        Text(value, color = valueColor, style = AlbumText.Heading)
    }
}

// Rows are full-bleed (edge to edge); only the divider above the first row is inset,
// matching the frame. Callers must NOT apply horizontal padding around this.
@Composable
fun TrackList(tracks: List<Track>, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        HorizontalDivider(Modifier.padding(horizontal = 20.dp), color = VynlColors.BorderMuted)
        tracks.forEach { TrackRow(it) }
    }
}

@Composable
private fun TrackRow(track: Track) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                track.number.toString(),
                modifier = Modifier.width(16.dp),
                color = VynlColors.TextSecondary,
                style = AlbumText.Caption,
                textAlign = TextAlign.Center
            )
            Text(
                track.title,
                modifier = Modifier.weight(1f).padding(start = 12.dp),
                color = VynlColors.TextPrimary,
                style = AlbumText.Body
            )
            Text(
                "%d:%02d".format(track.durationSeconds / 60, track.durationSeconds % 60),
                color = VynlColors.TextSecondary,
                style = AlbumText.Caption
            )
        }
        HorizontalDivider(color = VynlColors.BorderMuted)
    }
}

@Composable
fun ReviewsSection(reviews: List<Review>, onWriteReviewClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Recent Reviews", color = VynlColors.TextPrimary, style = AlbumText.Heading)
            Text(
                "Write a review",
                color = VynlColors.Accent,
                style = AlbumText.Body,
                modifier = Modifier.clickable(onClick = onWriteReviewClick)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            reviews.forEach { ReviewCard(it) }
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(VynlColors.Surface, CardShape)
            .border(1.dp, VynlColors.BorderMuted, CardShape)
            .padding(17.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(32.dp)
                    .background(VynlColors.SurfaceVariant, CircleShape)
                    .border(1.dp, VynlColors.BorderMuted, CircleShape)
                    .padding(1.dp)
                    .clip(CircleShape)
            ) {
                // Swap point for AsyncImage(model = review.avatarUrl)
                ImagePlaceholder(Modifier.fillMaxSize())
            }
            Column(Modifier.padding(start = 12.dp)) {
                Text(review.authorName, color = VynlColors.TextPrimary, style = AlbumText.BodyMedium)
                Text(review.handle, color = VynlColors.TextSecondary, style = AlbumText.Caption)
            }
            ReadOnlyStars(review.stars, Modifier.padding(start = 12.dp))
        }
        Text(
            review.text,
            color = VynlColors.TextReview,
            style = AlbumText.Body,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// Display-only stars for review cards. (StarRatingBar is the interactive picker and stays untouched.)
// No outlined icons available, so empty stars are the filled star at low alpha.
@Composable
private fun ReadOnlyStars(stars: Int, modifier: Modifier = Modifier) {
    Row(modifier) {
        for (position in 1..5) {
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = if (position <= stars) VynlColors.TextPrimary else VynlColors.TextPrimary.copy(alpha = 0.25f),
                modifier = Modifier.size(13.dp)
            )
        }
    }
}
