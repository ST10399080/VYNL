package com.example.vynl_app.ui.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.data.list.CustomList
import com.example.vynl_app.ui.theme.VynlColors

private val CardShape = RoundedCornerShape(8.dp)

@Composable
fun ListsScreen(
    viewModel: ListsViewModel,
    onBack: () -> Unit = {},
    onListClick: (String) -> Unit = {}
) {
    val lists by viewModel.lists.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp).padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = VynlColors.TextPrimary)
            }
            Text(
                "Lists",
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                color = VynlColors.TextPrimary,
                fontSize = 24.sp
            )
            OutlinedButton(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = VynlColors.TextPrimary)
            ) {
                Text("Create List")
            }
        }

        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(lists) { list -> ListCard(list, onClick = { onListClick(list.id) }) }
        }
    }

    if (showCreateDialog) {
        var newListName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("New list name") },
            text = {
                OutlinedTextField(value = newListName, onValueChange = { newListName = it }, singleLine = true)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newListName.isNotBlank()) {
                            viewModel.onListCreated(newListName)
                            showCreateDialog = false
                        }
                    }
                ) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun ListCard(list: CustomList, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(VynlColors.Surface, CardShape)
            .padding(15.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoverMosaic(itemCount = list.albumIds.size)
        Column(Modifier.weight(1f)) {
            Text(list.title, color = VynlColors.TextPrimary, fontSize = 16.sp)
            Spacer(Modifier.height(2.dp))
            Text(
                if (list.albumIds.size == 1) "1 album" else "${list.albumIds.size} albums",
                color = VynlColors.TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

// Up to a 2x2 grid of placeholder squares standing in for cover art (no image loader yet,
// no cover URLs stored on CustomList either). Fewer squares if the list has fewer albums.
@Composable
private fun CoverMosaic(itemCount: Int) {
    val squares = itemCount.coerceIn(0, 4)
    Column(
        Modifier.size(80.dp).background(VynlColors.SurfaceVariant, RoundedCornerShape(4.dp)).padding(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        for (row in 0..1) {
            Row(Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                for (col in 0..1) {
                    val index = row * 2 + col
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(if (index < squares) VynlColors.Accent.copy(alpha = 0.35f) else VynlColors.Surface)
                    )
                }
            }
        }
    }
}
