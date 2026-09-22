package com.example.vynl_app.ui.listdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vynl_app.ui.theme.VynlColors

@Composable
fun ListDetailScreen(
    viewModel: ListDetailViewModel,
    onBack: () -> Unit = {},
    onAlbumClick: (String) -> Unit = {}
) {
    val list by viewModel.list.collectAsState()
    val albums by viewModel.albums.collectAsState()

    Column(Modifier.fillMaxSize()) {
        ListDetailTopBar(onBack = onBack)

        val detail = list
        if (detail == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VynlColors.Accent)
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ListDetailHeader(detail)
                Column {
                    albums.forEachIndexed { index, entry ->
                        if (index > 0) HorizontalDivider(color = VynlColors.BorderMuted)
                        ListAlbumRow(
                            entry = entry,
                            onClick = { onAlbumClick(entry.albumId) },
                            onRemoveClick = { viewModel.onAlbumRemoved(entry.albumId) }
                        )
                    }
                }
            }
        }
    }
}
