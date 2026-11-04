package com.example.vynl_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.vynl_app.data.account.FakeAccountRepository
import com.example.vynl_app.data.album.FakeAlbumRepository
import com.example.vynl_app.data.album.FakeSearchRepository
import com.example.vynl_app.data.artist.FakeArtistRepository
import com.example.vynl_app.data.artist.FakeArtistSearchRepository
import com.example.vynl_app.data.home.FakeHomeFeedRepository
import com.example.vynl_app.data.list.FakeListRepository
import com.example.vynl_app.data.rating.FakeRatingRepository
import com.example.vynl_app.data.review.FakeReviewRepository
import com.example.vynl_app.data.settings.AppSettings
import com.example.vynl_app.data.settings.DataStoreSettingsRepository
import com.example.vynl_app.data.settings.ThemeMode
import com.example.vynl_app.data.settings.settingsDataStore
import com.example.vynl_app.data.song.FakeSongSearchRepository
import com.example.vynl_app.data.tierlist.FakeTierListRepository
import com.example.vynl_app.data.tierlist.TierListMode
import com.example.vynl_app.notifications.NotificationHelper
import com.example.vynl_app.notifications.PushRegistrar
import com.example.vynl_app.ui.albumdetail.AlbumDetailScreen
import com.example.vynl_app.ui.albumdetail.AlbumDetailViewModel
import com.example.vynl_app.ui.artistdetail.ArtistDetailScreen
import com.example.vynl_app.ui.artistdetail.ArtistDetailViewModel
import com.example.vynl_app.ui.homefeed.HomeFeedScreen
import com.example.vynl_app.ui.homefeed.HomeFeedViewModel
import com.example.vynl_app.ui.listdetail.ListDetailScreen
import com.example.vynl_app.ui.listdetail.ListDetailViewModel
import com.example.vynl_app.ui.lists.ListsScreen
import com.example.vynl_app.ui.lists.ListsViewModel
import com.example.vynl_app.ui.navigation.AlbumDetail
import com.example.vynl_app.ui.navigation.ArtistDetail
import com.example.vynl_app.ui.navigation.HomeFeed
import com.example.vynl_app.ui.navigation.ListDetail
import com.example.vynl_app.ui.navigation.Lists
import com.example.vynl_app.ui.navigation.Search
import com.example.vynl_app.ui.navigation.Settings
import com.example.vynl_app.ui.navigation.TierListBuilder
import com.example.vynl_app.ui.navigation.TierListViewer
import com.example.vynl_app.ui.search.SearchScreen
import com.example.vynl_app.ui.search.SearchViewModel
import com.example.vynl_app.ui.settings.SettingsScreen
import com.example.vynl_app.ui.settings.SettingsViewModel
import com.example.vynl_app.ui.theme.VynlTheme
import com.example.vynl_app.ui.tierlistbuilder.TierListBuilderScreen
import com.example.vynl_app.ui.tierlistbuilder.TierListBuilderViewModel
import com.example.vynl_app.ui.tierlistviewer.TierListViewerScreen
import com.example.vynl_app.ui.tierlistviewer.TierListViewerViewModel

// AppCompatActivity (not ComponentActivity) so per-app language switching also works below Android 13.
class MainActivity : AppCompatActivity() {
    // Album to open because a notification was tapped; consumed once by the NavHost below.
    private var pendingAlbumId by mutableStateOf<String?>(null)

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingAlbumId = intent.getStringExtra(NotificationHelper.EXTRA_ALBUM_ID)
        Log.d("VYNL_FCM", "onNewIntent albumId=$pendingAlbumId")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Only on a fresh launch — a rotation/language change re-delivers the same intent.
        if (savedInstanceState == null) {
            pendingAlbumId = intent.getStringExtra(NotificationHelper.EXTRA_ALBUM_ID)
            Log.d("VYNL_FCM", "onCreate albumId=$pendingAlbumId")
        }
        PushRegistrar.logToken(applicationContext)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            // Settings live on-device (DataStore); the theme is applied here so every screen follows it.
            val settingsRepository = remember { DataStoreSettingsRepository(applicationContext.settingsDataStore) }
            val accountRepository = remember { FakeAccountRepository() }
            // Keep FCM topic subscriptions in step with the user's notification settings.
            LaunchedEffect(settingsRepository) {
                settingsRepository.settings.collect { PushRegistrar.syncTopics(applicationContext, it) }
            }
            val settings by settingsRepository.settings.collectAsState(initial = AppSettings())
            val darkTheme = when (settings.themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }
            // Status/navigation bar icons must flip with the theme.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT) { darkTheme }
                )
                onDispose { }
            }
            VynlTheme(themeMode = settings.themeMode) {
                val navController = rememberNavController()
                // Shared across Album Detail's Add to List sheet, Lists, and List Detail so
                // a membership change made from one screen is visible from the others.
                val listRepository = remember { FakeListRepository() }
                // Shared across the Tier List Builder and Viewer so a placement made in the
                // Builder is visible immediately from Preview.
                val tierListRepository = remember { FakeTierListRepository() }
                val homeFeedRepository = remember { FakeHomeFeedRepository() }

                // A tapped notification asks to open an album.
                LaunchedEffect(pendingAlbumId) {
                    pendingAlbumId?.let { albumId ->
                        navController.navigate(AlbumDetail(albumId))
                        pendingAlbumId = null
                    }
                }

                NavHost(navController = navController, startDestination = HomeFeed) {
                    composable<HomeFeed> {
                        val viewModel: HomeFeedViewModel = viewModel {
                            HomeFeedViewModel(homeFeedRepository = homeFeedRepository)
                        }
                        HomeFeedScreen(
                            viewModel = viewModel,
                            onSearchClick = { navController.navigate(Search) },
                            onAlbumClick = { albumId -> navController.navigate(AlbumDetail(albumId)) },
                            onArtistClick = { artistId -> navController.navigate(ArtistDetail(artistId)) },
                            onMyListsClick = { navController.navigate(Lists) },
                            onSettingsClick = { navController.navigate(Settings) }
                        )
                    }

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
                            listRepository = listRepository,
                            onBack = { navController.popBackStack() },
                            onSearchClick = { navController.navigate(Search) },
                            onArtistClick = { artistId -> navController.navigate(ArtistDetail(artistId)) },
                            onMyListsClick = { navController.navigate(Lists) }
                        )
                    }

                    composable<Settings> {
                        val viewModel: SettingsViewModel = viewModel {
                            SettingsViewModel(
                                settingsRepository = settingsRepository,
                                accountRepository = accountRepository
                            )
                        }
                        SettingsScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable<Lists> {
                        val viewModel: ListsViewModel = viewModel {
                            ListsViewModel(currentUserId = "demo-user", listRepository = listRepository)
                        }
                        ListsScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onListClick = { listId -> navController.navigate(ListDetail(listId)) }
                        )
                    }

                    composable<ListDetail> { backStackEntry ->
                        val args = backStackEntry.toRoute<ListDetail>()
                        val viewModel: ListDetailViewModel = viewModel {
                            ListDetailViewModel(
                                listId = args.listId,
                                listRepository = listRepository,
                                albumRepository = FakeAlbumRepository(),
                                ratingRepository = FakeRatingRepository()
                            )
                        }
                        ListDetailScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onAlbumClick = { albumId -> navController.navigate(AlbumDetail(albumId)) }
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
                            onBack = { navController.popBackStack() },
                            onReleaseClick = { albumId -> navController.navigate(AlbumDetail(albumId)) },
                            onTrackAlbumClick = { albumId -> navController.navigate(AlbumDetail(albumId)) },
                            onRankAlbumsClick = { navController.navigate(TierListBuilder(args.artistId, "ALBUMS")) },
                            onRankSongsClick = { navController.navigate(TierListBuilder(args.artistId, "SONGS")) }
                        )
                    }

                    composable<TierListBuilder> { backStackEntry ->
                        val args = backStackEntry.toRoute<TierListBuilder>()
                        val viewModel: TierListBuilderViewModel = viewModel {
                            TierListBuilderViewModel(
                                artistId = args.artistId,
                                mode = TierListMode.valueOf(args.mode),
                                currentUserId = "demo-user",
                                tierListRepository = tierListRepository,
                                artistRepository = FakeArtistRepository()
                            )
                        }
                        TierListBuilderScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onPreviewClick = { tierListId -> navController.navigate(TierListViewer(tierListId)) }
                        )
                    }

                    composable<TierListViewer> { backStackEntry ->
                        val args = backStackEntry.toRoute<TierListViewer>()
                        val viewModel: TierListViewerViewModel = viewModel {
                            TierListViewerViewModel(
                                tierListId = args.tierListId,
                                tierListRepository = tierListRepository,
                                artistRepository = FakeArtistRepository()
                            )
                        }
                        TierListViewerScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
