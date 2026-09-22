package com.example.vynl_app.ui.albumdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.ui.rating.StarRatingBar
import com.example.vynl_app.ui.theme.VynlColors

private const val REVIEW_MAX_LENGTH = 500

private object ComposerText {
    val Label = TextStyle(fontSize = 12.sp, letterSpacing = 1.2.sp)
    val Body = TextStyle(fontSize = 14.sp, letterSpacing = (-0.42).sp)
    val Caption = TextStyle(fontSize = 12.sp, letterSpacing = (-0.396).sp)
}

private val CardShape = RoundedCornerShape(10.dp)

// Content of the Review Composer sheet (Figma frame 1:669). Callers wrap this in a
// ModalBottomSheet, same pattern as the existing star-rating sheet in AlbumDetailScreen.
// Not built: the frame's "Sonic Tags" picker (no tag data domain exists anywhere in the
// app yet, and it's outside the design doc's section 8 scope) — deliberately omitted
// rather than stubbed, per the Figma-vs-doc divergence noted for this screen.
@Composable
fun ReviewComposerSheet(
    albumTitle: String,
    albumArtist: String,
    albumYear: Int,
    currentRating: Int,
    onRatingSelected: (Int) -> Unit,
    reviewText: String,
    onReviewTextChanged: (String) -> Unit,
    shareToFeed: Boolean,
    onShareToFeedChanged: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onPostReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .navigationBarsPadding()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("WRITE A REVIEW", color = VynlColors.TextSecondary, style = ComposerText.Label)
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp).background(VynlColors.SurfaceVariant, CircleShape)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Dismiss", tint = VynlColors.TextPrimary, modifier = Modifier.size(14.dp))
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .background(VynlColors.SurfaceVariant, CardShape)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(48.dp).background(VynlColors.Surface, RoundedCornerShape(7.dp)))
            Column {
                Text(albumTitle, color = VynlColors.TextPrimary, style = ComposerText.Body)
                Row {
                    Text(albumArtist, color = VynlColors.Accent, style = ComposerText.Caption)
                    Text(" · $albumYear", color = VynlColors.TextSecondary, style = ComposerText.Caption)
                }
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .background(VynlColors.SurfaceVariant.copy(alpha = 0.4f), CardShape)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("SELECT RATING", color = VynlColors.TextSecondary, style = ComposerText.Label)
            StarRatingBar(currentRating = currentRating, onRatingSelected = onRatingSelected)
        }

        Column(
            Modifier
                .fillMaxWidth()
                .background(VynlColors.SurfaceVariant, CardShape)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("CRITIQUE", color = VynlColors.TextSecondary, style = ComposerText.Label)
                Text("${reviewText.length} / $REVIEW_MAX_LENGTH", color = VynlColors.TextSecondary, style = ComposerText.Caption)
            }
            OutlinedTextField(
                value = reviewText,
                onValueChange = { if (it.length <= REVIEW_MAX_LENGTH) onReviewTextChanged(it) },
                placeholder = { Text("What did you think?") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Share to Community Feed", color = VynlColors.TextPrimary, style = ComposerText.Body)
            // TODO: no Activity Feed exists yet (design doc section 9) to actually share into.
            // Toggle works visually; its state isn't read by onPostReview below.
            Switch(
                checked = shareToFeed,
                onCheckedChange = onShareToFeedChanged,
                colors = SwitchDefaults.colors(checkedTrackColor = VynlColors.Accent)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Button(
                onClick = onPostReview,
                enabled = currentRating > 0,
                shape = CardShape,
                colors = ButtonDefaults.buttonColors(containerColor = VynlColors.TextPrimary, contentColor = VynlColors.OnAccent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Post Review")
            }
            Text(
                "Saved to your personal listening catalog automatically",
                color = VynlColors.TextSecondary,
                style = ComposerText.Caption,
                textAlign = TextAlign.Center
            )
        }
    }
}
