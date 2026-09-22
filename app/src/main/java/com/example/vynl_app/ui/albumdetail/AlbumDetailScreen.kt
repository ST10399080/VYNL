package com.example.vynl_app.ui.albumdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vynl_app.data.list.ListRepository
import com.example.vynl_app.ui.list.AddToListSheet
import com.example.vynl_app.ui.list.AddToListViewModel
import com.example.vynl_app.ui.rating.StarRatingBar
import com.example.vynl_app.ui.theme.VynlColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    viewModel: AlbumDetailViewModel,
    listRepository: ListRepository,
    onBack: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onArtistClick: (String) -> Unit = {},
    onMyListsClick: () -> Unit = {}
) {
    val userRating by viewModel.userRating.collectAsState()
    val summary by viewModel.communityScore.collectAsState()
    val album by viewModel.album.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    var showRatingSheet by remember { mutableStateOf(false) } // controls whether the bottom sheet is open
    var showReviewSheet by remember { mutableStateOf(false) }
    var reviewDraftStars by remember { mutableStateOf(0) }
    var reviewDraftText by remember { mutableStateOf("") }
    var reviewDraftShareToFeed by remember { mutableStateOf(false) }
    var showAddToListSheet by remember { mutableStateOf(false) }
    val addToListViewModel: AddToListViewModel = viewModel {
        AddToListViewModel(currentUserId = viewModel.currentUserId, listRepository = listRepository)
    }

    Column(Modifier.fillMaxSize()) {
        AlbumDetailTopBar(
            onBack = onBack,
            onSearchClick = onSearchClick,
            onAddToListClick = { showAddToListSheet = true },
            onMyListsClick = onMyListsClick
        )

        val detail = album
        if (detail == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VynlColors.Accent)
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 20.dp)
                    .navigationBarsPadding()
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                AlbumHeader(
                    album = detail,
                    // Button swaps text once the user has rated, matching the design decision from earlier
                    rateLabel = if (userRating != null) "Your rating: $userRating stars" else "Rate album",
                    onRateClick = { showRatingSheet = true },
                    onPlayClick = { /* playback isn't built yet */ },
                    onArtistClick = { onArtistClick(detail.artistId) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                StatsSection(
                    communityPercentage = summary.percentage,
                    criticScore = detail.criticScore,
                    isCommunityFavorite = detail.isCommunityFavorite,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                TrackList(detail.tracks)
                ReviewsSection(
                    reviews = reviews,
                    onWriteReviewClick = { showReviewSheet = true },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }

    // Bottom sheet only exists in the UI tree while showRatingSheet is true
    if (showRatingSheet) {
        ModalBottomSheet(onDismissRequest = { showRatingSheet = false }) {
            Column(Modifier.padding(24.dp)) {
                StarRatingBar(
                    currentRating = userRating ?: 0,
                    onRatingSelected = { viewModel.onRatingSubmitted(it) }
                )
            }
        }
    }

    val detailForReview = album
    if (showReviewSheet && detailForReview != null) {
        fun closeReviewSheet() {
            showReviewSheet = false
            reviewDraftStars = 0
            reviewDraftText = ""
            reviewDraftShareToFeed = false
        }

        ModalBottomSheet(onDismissRequest = { closeReviewSheet() }) {
            ReviewComposerSheet(
                albumTitle = detailForReview.title,
                albumArtist = detailForReview.artist,
                albumYear = detailForReview.year,
                currentRating = reviewDraftStars,
                onRatingSelected = { reviewDraftStars = it },
                reviewText = reviewDraftText,
                onReviewTextChanged = { reviewDraftText = it },
                shareToFeed = reviewDraftShareToFeed,
                onShareToFeedChanged = { reviewDraftShareToFeed = it },
                onDismiss = { closeReviewSheet() },
                onPostReview = {
                    viewModel.onReviewSubmitted(reviewDraftStars, reviewDraftText)
                    closeReviewSheet()
                }
            )
        }
    }

    val detailForAddToList = album
    if (showAddToListSheet && detailForAddToList != null) {
        val lists by addToListViewModel.lists.collectAsState()
        val albumId = detailForAddToList.id
        ModalBottomSheet(onDismissRequest = { showAddToListSheet = false }) {
            AddToListSheet(
                albumId = albumId,
                lists = lists,
                onMembershipToggled = { listId, isMember ->
                    addToListViewModel.onMembershipToggled(listId, albumId, isMember)
                },
                onCreateList = { title -> addToListViewModel.onListCreated(title, albumId) },
                onDismiss = { showAddToListSheet = false }
            )
        }
    }
}
