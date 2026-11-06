package com.example.vynl_app.ui.rating

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private fun descriptorFor(stars: Int): String = when (stars) {
    0 -> "Not rated"
    1 -> "Poor"
    2 -> "Fair"
    3 -> "Good"
    4 -> "Outstanding"
    5 -> "Masterpiece"
    else -> ""
}

@Composable
fun StarRatingBar(
    currentRating: Int,
    onRatingSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (position in 1..5) {
                val filled = position <= currentRating
                Text(
                    text = if (filled) "\u2605" else "\u2606", // filled star / outline star
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable { onRatingSelected(position) }
                )
            }
        }
        Text(if (currentRating > 0) "${currentRating}.0 / 5.0  \u00B7  ${descriptorFor(currentRating)}" else "SELECT RATING")
    }
}