package com.example.vynl_app.ui.homefeed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.data.home.FeaturedAlbum
import com.example.vynl_app.ui.theme.VynlColors

private val CardShape = RoundedCornerShape(8.dp)

// Placeholder until an image loader (Coil) is added, same swap point used everywhere
// else in the app.
@Composable
private fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Box(modifier.background(Brush.linearGradient(listOf(VynlColors.Accent.copy(alpha = 0.55f), VynlColors.SurfaceVariant))))
}

@Composable
fun HeroCard(
    album: FeaturedAlbum,
    onAlbumClick: () -> Unit,
    onArtistClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .clickable(onClick = onAlbumClick)
            .background(VynlColors.Surface, CardShape)
    ) {
        ImagePlaceholder(Modifier.fillMaxSize())
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(VynlColors.Background.copy(alpha = 0f), VynlColors.Background.copy(alpha = 0.85f))))
        )
        Text(
            "#1 THIS WEEK",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .background(VynlColors.Background.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            color = VynlColors.TextPrimary,
            fontSize = 11.sp
        )
        Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
            Text(album.title, color = VynlColors.TextPrimary, fontSize = 20.sp)
            Text(
                album.artist,
                color = VynlColors.Accent,
                fontSize = 14.sp,
                modifier = Modifier.clickable(onClick = onArtistClick)
            )
        }
    }
}

@Composable
fun FeaturedAlbumRow(
    album: FeaturedAlbum,
    rank: Int,
    onAlbumClick: () -> Unit,
    onArtistClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable(onClick = onAlbumClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(48.dp).background(VynlColors.SurfaceVariant, RoundedCornerShape(6.dp)))
        Column {
            Text(album.title, color = VynlColors.TextPrimary, fontSize = 14.sp)
            Row {
                Text(
                    album.artist,
                    color = VynlColors.Accent,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable(onClick = onArtistClick)
                )
                Text(" · #$rank this week", color = VynlColors.TextSecondary, fontSize = 12.sp)
            }
        }
    }
}
