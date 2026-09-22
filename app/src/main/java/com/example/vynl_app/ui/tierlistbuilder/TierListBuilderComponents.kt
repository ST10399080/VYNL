package com.example.vynl_app.ui.tierlistbuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.data.tierlist.TierRank
import com.example.vynl_app.ui.theme.VynlColors

private val CardShape = RoundedCornerShape(7.dp)

// Descending "specialness" from S to D, same idea the Figma frame draws (S/A/B get a
// distinct accent border, lower tiers fade toward neutral) — D isn't in the pulled frame,
// so it continues that fade using the app's own muted-border token rather than inventing
// an unrelated color.
private fun tierColor(rank: TierRank): Color = when (rank) {
    TierRank.S -> Color(0xFFFF00B8)
    TierRank.A -> Color(0xFFFFCB45)
    TierRank.B -> Color(0xFFB8FF45)
    TierRank.C -> Color(0xFF505050)
    TierRank.D -> VynlColors.BorderMuted
}

private fun tierLabel(rank: TierRank): String = when (rank) {
    TierRank.S -> "ELITE"
    TierRank.A -> "HEAVY"
    TierRank.B -> "SOLID"
    TierRank.C -> "MID"
    TierRank.D -> "WEAK"
}

@Composable
fun TierRow(
    rank: TierRank,
    items: List<TierListDisplayItem>,
    selectedItemId: String?,
    onItemClick: (String) -> Unit,
    onRowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable(onClick = onRowClick)
            .background(VynlColors.Surface, CardShape)
            .border(1.dp, VynlColors.BorderMuted, CardShape)
            .padding(11.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            Modifier
                .width(36.dp)
                .background(VynlColors.SurfaceVariant, RoundedCornerShape(7.dp))
                .border(1.dp, tierColor(rank), RoundedCornerShape(7.dp))
                .padding(vertical = 9.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(rank.name, color = VynlColors.TextPrimary, fontSize = 20.sp)
            Text(tierLabel(rank), color = VynlColors.TextSecondary, fontSize = 8.sp)
        }

        if (items.isEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .size(64.dp)
                    .border(1.dp, VynlColors.BorderMuted, RoundedCornerShape(7.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Tap a card to assign", color = VynlColors.TextSecondary, fontSize = 11.sp)
            }
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { item ->
                    TierListItemCard(
                        item = item,
                        isSelected = item.itemId == selectedItemId,
                        onClick = { onItemClick(item.itemId) }
                    )
                }
            }
        }
    }
}

@Composable
fun TrayRow(
    items: List<TierListDisplayItem>,
    selectedItemId: String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Unranked", color = VynlColors.TextPrimary, fontSize = 16.sp)
            Text(
                if (items.size == 1) "1 remaining" else "${items.size} remaining",
                color = VynlColors.TextSecondary,
                fontSize = 11.sp
            )
        }
        if (items.isEmpty()) {
            Text("Everything's been ranked.", color = VynlColors.TextSecondary, fontSize = 12.sp)
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { item ->
                    TierListItemCard(
                        item = item,
                        isSelected = item.itemId == selectedItemId,
                        onClick = { onItemClick(item.itemId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TierListItemCard(item: TierListDisplayItem, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        Modifier
            .width(96.dp)
            .clickable(onClick = onClick)
            .background(VynlColors.SurfaceVariant, CardShape)
            .border(
                if (isSelected) 2.dp else 1.dp,
                if (isSelected) VynlColors.Accent else VynlColors.BorderMuted,
                CardShape
            )
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(Modifier.size(64.dp).background(VynlColors.Surface, RoundedCornerShape(5.dp)))
        Text(item.title, color = VynlColors.TextPrimary, fontSize = 12.sp, maxLines = 1)
        Text(item.subtitle, color = VynlColors.TextSecondary, fontSize = 10.sp, maxLines = 1)
    }
}

// The live instruction line above the tiers, plus the "Move to Unranked" / "Cancel" actions
// that only appear once something is selected.
@Composable
fun SelectionInstructionRow(
    selectedTitle: String?,
    isSelectedItemPlaced: Boolean,
    onMoveToUnranked: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            if (selectedTitle != null) "Tap a tier to place “$selectedTitle”" else "Tap a card, then tap a tier to place it",
            color = VynlColors.TextSecondary,
            fontSize = 13.sp
        )
        if (selectedTitle != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (isSelectedItemPlaced) {
                    Text(
                        "Move to Unranked",
                        color = VynlColors.Accent,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable(onClick = onMoveToUnranked)
                    )
                }
                Text(
                    "Cancel",
                    color = VynlColors.TextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable(onClick = onCancel)
                )
            }
        }
    }
}
