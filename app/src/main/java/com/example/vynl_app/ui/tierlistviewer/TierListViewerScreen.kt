package com.example.vynl_app.ui.tierlistviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.data.tierlist.TierRank
import com.example.vynl_app.ui.theme.VynlColors
import com.example.vynl_app.ui.tierlistbuilder.TierListDisplayItem

private val CardShape = RoundedCornerShape(7.dp)

private fun tierColor(rank: TierRank): Color = when (rank) {
    TierRank.S -> Color(0xFFFF00B8)
    TierRank.A -> Color(0xFFFFCB45)
    TierRank.B -> Color(0xFFB8FF45)
    TierRank.C -> Color(0xFF505050)
    TierRank.D -> VynlColors.BorderMuted
}

@Composable
fun TierListViewerScreen(viewModel: TierListViewerViewModel, onBack: () -> Unit = {}) {
    val tierList by viewModel.tierList.collectAsState()
    val displayItems by viewModel.displayItems.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth().background(VynlColors.Background).windowInsetsPadding(WindowInsets.statusBars)) {
            Box(Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 4.dp)) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart).alpha(0.8f)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = VynlColors.TextPrimary, modifier = Modifier.size(16.dp))
                }
                Text("Tier List", modifier = Modifier.align(Alignment.Center), color = VynlColors.TextPrimary, fontSize = 16.sp)
            }
        }

        val list = tierList
        if (list == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VynlColors.Accent)
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(list.title, color = VynlColors.TextPrimary, fontSize = 24.sp)

                TierRank.entries.forEach { rank ->
                    val items = list.items.filter { it.tier == rank }.mapNotNull { displayItems[it.itemId] }
                    ViewerTierRow(rank, items)
                }
            }
        }
    }
}

@Composable
private fun ViewerTierRow(rank: TierRank, items: List<TierListDisplayItem>) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(VynlColors.Surface, CardShape)
            .border(1.dp, VynlColors.BorderMuted, CardShape)
            .padding(11.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            Modifier
                .width(36.dp)
                .size(36.dp)
                .background(VynlColors.SurfaceVariant, RoundedCornerShape(7.dp))
                .border(1.dp, tierColor(rank), RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(rank.name, color = VynlColors.TextPrimary, fontSize = 18.sp)
        }

        if (items.isEmpty()) {
            Box(Modifier.fillMaxWidth().size(36.dp), contentAlignment = Alignment.CenterStart) {
                Text("—", color = VynlColors.TextSecondary, fontSize = 14.sp)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items.forEach { item ->
                    Text(item.title, color = VynlColors.TextPrimary, fontSize = 14.sp)
                }
            }
        }
    }
}
