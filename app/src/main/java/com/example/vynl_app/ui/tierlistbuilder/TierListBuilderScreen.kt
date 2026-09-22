package com.example.vynl_app.ui.tierlistbuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.data.tierlist.TierRank
import com.example.vynl_app.ui.theme.VynlColors

@Composable
fun TierListBuilderScreen(
    viewModel: TierListBuilderViewModel,
    onBack: () -> Unit = {},
    onPreviewClick: (String) -> Unit = {}
) {
    val tierList by viewModel.tierList.collectAsState()
    val displayItems by viewModel.displayItems.collectAsState()
    val selectedItemId by viewModel.selectedItemId.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth().background(VynlColors.Background).windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Box(Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 4.dp)) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart).alpha(0.8f)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = VynlColors.TextPrimary, modifier = Modifier.size(16.dp))
                }
                Text("Tier List Builder", modifier = Modifier.align(Alignment.Center), color = VynlColors.TextPrimary, fontSize = 16.sp)
                TextButton(
                    onClick = { tierList?.let { onPreviewClick(it.id) } },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) { Text("Preview", color = VynlColors.Accent) }
            }
        }

        val list = tierList
        if (list == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VynlColors.Accent)
            }
        } else {
            val itemsByTier = TierRank.entries.associateWith { rank ->
                list.items.filter { it.tier == rank }.mapNotNull { displayItems[it.itemId] }
            }
            val unranked = list.items.filter { it.tier == null }.mapNotNull { displayItems[it.itemId] }
            val selectedTitle = selectedItemId?.let { displayItems[it]?.title }
            val selectedIsPlaced = selectedItemId != null &&
                list.items.find { it.itemId == selectedItemId }?.tier != null

            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(list.title, color = VynlColors.TextPrimary, fontSize = 24.sp)

                SelectionInstructionRow(
                    selectedTitle = selectedTitle,
                    isSelectedItemPlaced = selectedIsPlaced,
                    onMoveToUnranked = viewModel::onMoveToUnrankedTapped,
                    onCancel = viewModel::onCancelSelection
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    TierRank.entries.forEach { rank ->
                        TierRow(
                            rank = rank,
                            items = itemsByTier[rank].orEmpty(),
                            selectedItemId = selectedItemId,
                            onItemClick = viewModel::onItemTapped,
                            onRowClick = { viewModel.onTierTapped(rank) }
                        )
                    }
                }

                TrayRow(
                    items = unranked,
                    selectedItemId = selectedItemId,
                    onItemClick = viewModel::onItemTapped
                )
            }
        }
    }
}
