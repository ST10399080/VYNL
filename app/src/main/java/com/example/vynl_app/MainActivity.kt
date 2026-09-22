package com.example.vynl_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vynl_app.data.album.FakeAlbumRepository
import com.example.vynl_app.data.rating.FakeRatingRepository
import com.example.vynl_app.data.review.FakeReviewRepository
import com.example.vynl_app.ui.albumdetail.AlbumDetailScreen
import com.example.vynl_app.ui.albumdetail.AlbumDetailViewModel
import com.example.vynl_app.ui.theme.VynlTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            VynlTheme {
                // viewModel { } keeps one instance across rotation instead of rebuilding it on recomposition
                val viewModel: AlbumDetailViewModel = viewModel {
                    AlbumDetailViewModel(
                        albumId = "1",
                        currentUserId = "demo-user",
                        ratingRepository = FakeRatingRepository(),
                        albumRepository = FakeAlbumRepository(),
                        reviewRepository = FakeReviewRepository()
                    )
                }
                AlbumDetailScreen(viewModel)
            }
        }
    }
}
