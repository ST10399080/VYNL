package com.example.vynl_app.ui.rating

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// A fake, throwaway state var just for previewing. Real screens won't use
// remember/mutableIntStateOf like this, they'll get the value from a ViewModel.
@Preview(showBackground = true)
@Composable
fun StarRatingBarStandalonePreview() {
    var rating by remember { mutableIntStateOf(0) }
    StarRatingBar(
        currentRating = rating,
        onRatingSelected = { rating = it }, // tapping a star updates "rating", which reruns this function
        modifier = Modifier.padding(24.dp)
    )
}
