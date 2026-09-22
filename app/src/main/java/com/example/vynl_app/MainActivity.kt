package com.example.vynl_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.vynl_app.data.album.FakeAlbumRepository
import com.example.vynl_app.data.album.FakeSearchRepository
import com.example.vynl_app.data.artist.FakeArtistRepository
import com.example.vynl_app.data.artist.FakeArtistSearchRepository
import com.example.vynl_app.data.rating.FakeRatingRepository
import com.example.vynl_app.data.review.FakeReviewRepository
import com.example.vynl_app.data.song.FakeSongSearchRepository
import com.example.vynl_app.ui.albumdetail.AlbumDetailScreen
import com.example.vynl_app.ui.albumdetail.AlbumDetailViewModel
import com.example.vynl_app.ui.artistdetail.ArtistDetailScreen
import com.example.vynl_app.ui.artistdetail.ArtistDetailViewModel
import com.example.vynl_app.ui.navigation.AlbumDetail
import com.example.vynl_app.ui.navigation.ArtistDetail
import com.example.vynl_app.ui.navigation.Search
import com.example.vynl_app.ui.search.SearchScreen
import com.example.vynl_app.ui.search.SearchViewModel
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
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Search) {
                    composable<Search> {
                        val viewModel: SearchViewModel = viewModel {
                            SearchViewModel(
                                albumRepository = FakeSearchRepository(),
                                songRepository = FakeSongSearchRepository(),
                                artistRepository = FakeArtistSearchRepository()
                            )
                        }
                        SearchScreen(
                            viewModel = viewModel,
                            onAlbumClick = { albumId -> navController.navigate(AlbumDetail(albumId)) },
                            onArtistClick = { artistId -> navController.navigate(ArtistDetail(artistId)) }
                        )
                    }

                    composable<AlbumDetail> { backStackEntry ->
                        val args = backStackEntry.toRoute<AlbumDetail>()
                        // viewModel { } keeps one instance across rotation instead of rebuilding it on recomposition
                        val viewModel: AlbumDetailViewModel = viewModel {
                            AlbumDetailViewModel(
                                albumId = args.albumId,
                                currentUserId = "demo-user",
                                ratingRepository = FakeRatingRepository(),
                                albumRepository = FakeAlbumRepository(),
                                reviewRepository = FakeReviewRepository()
                            )
                        }
                        AlbumDetailScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onSearchClick = { navController.navigate(Search) },
                            onArtistClick = { artistId -> navController.navigate(ArtistDetail(artistId)) }
                        )
                    }

                    composable<ArtistDetail> { backStackEntry ->
                        val args = backStackEntry.toRoute<ArtistDetail>()
                        val viewModel: ArtistDetailViewModel = viewModel {
                            ArtistDetailViewModel(
                                artistId = args.artistId,
                                artistRepository = FakeArtistRepository()
                            )
                        }
                        ArtistDetailScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
