package com.example.vynl_app.ui.listdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.data.list.CustomList
import com.example.vynl_app.ui.theme.VynlColors

private val CardShape = RoundedCornerShape(8.dp)

@Composable
fun ListDetailTopBar(onBack: () -> Unit, onShare: () -> Unit = {}) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(VynlColors.Background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        var showMenu by remember { mutableStateOf(false) }
        Box(Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 4.dp)) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart).alpha(0.8f)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = VynlColors.TextPrimary, modifier = Modifier.size(16.dp))
            }
            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                // TODO: share this list (design doc section 9, "Shareable list links") — no
                // deep-link scheme exists yet to build this against.
                IconButton(onClick = onShare, modifier = Modifier.alpha(0.8f)) {
                    Icon(Icons.Filled.Share, contentDescription = "Share list", tint = VynlColors.TextPrimary, modifier = Modifier.size(16.dp))
                }
                Box {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.alpha(0.8f)) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options", tint = VynlColors.TextPrimary, modifier = Modifier.size(16.dp))
                    }
                    // TODO: no menu contents specified anywhere (Figma frame 1:3431 never
                    // expands this menu, design doc doesn't say). Empty until a real need shows up.
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {}
                }
            }
        }
    }
}

@Composable
fun ListDetailHeader(list: CustomList, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Text(list.title, color = VynlColors.TextPrimary, fontSize = 24.sp)
        if (list.description.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(list.description, color = VynlColors.TextSecondary, fontSize = 14.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            if (list.albumIds.size == 1) "1 album" else "${list.albumIds.size} albums",
            color = VynlColors.TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ListAlbumRow(entry: ListAlbumEntry, onClick: () -> Unit, onRemoveClick: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(48.dp).background(VynlColors.SurfaceVariant, RoundedCornerShape(6.dp)))
        Column(Modifier.weight(1f)) {
            Text(entry.title, color = VynlColors.TextPrimary, fontSize = 14.sp)
            Text(entry.artist, color = VynlColors.Accent, fontSize = 12.sp)
        }
        Text(
            "${entry.ratingPercentage}%",
            color = VynlColors.TextPrimary,
            fontSize = 12.sp,
            modifier = Modifier
                .background(VynlColors.SurfaceVariant, RoundedCornerShape(7.dp))
                .padding(horizontal = 9.dp, vertical = 3.dp)
        )
        Box {
            IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Entry options", tint = VynlColors.TextSecondary, modifier = Modifier.size(16.dp))
            }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(
                    text = { Text("Remove from list") },
                    onClick = {
                        showMenu = false
                        onRemoveClick()
                    }
                )
            }
        }
    }
}
