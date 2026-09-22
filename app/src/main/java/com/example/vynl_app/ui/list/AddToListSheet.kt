package com.example.vynl_app.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vynl_app.data.list.CustomList
import com.example.vynl_app.ui.theme.VynlColors

private val Label = TextStyle(fontSize = 12.sp, letterSpacing = 1.2.sp)
private val Body = TextStyle(fontSize = 14.sp, letterSpacing = (-0.42).sp)
private val CardShape = RoundedCornerShape(10.dp)

// Content of the Add to List sheet. No Figma frame exists for this one (checked the whole
// file: nothing named "Add to List Sheet" despite the Figure 7 nav diagram naming it) — laid
// out in prose against the design doc's Custom_Lists description and this app's existing
// sheet conventions (ReviewComposerSheet, the rating sheet) instead. Callers wrap this in a
// ModalBottomSheet.
@Composable
fun AddToListSheet(
    albumId: String,
    lists: List<CustomList>,
    onMembershipToggled: (listId: String, isMember: Boolean) -> Unit,
    onCreateList: (title: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var newListName by remember { mutableStateOf("") }

    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .navigationBarsPadding()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("ADD TO LIST", color = VynlColors.TextSecondary, style = Label)
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp).background(VynlColors.SurfaceVariant, CircleShape)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Dismiss", tint = VynlColors.TextPrimary, modifier = Modifier.size(14.dp))
            }
        }

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = newListName,
                onValueChange = { newListName = it },
                placeholder = { Text("New list name") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    if (newListName.isNotBlank()) {
                        onCreateList(newListName)
                        newListName = ""
                    }
                },
                modifier = Modifier.size(48.dp).background(VynlColors.SurfaceVariant, CardShape)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create list", tint = VynlColors.TextPrimary)
            }
        }

        Column(Modifier.fillMaxWidth().background(VynlColors.Surface, CardShape)) {
            lists.forEachIndexed { index, list ->
                val isMember = albumId in list.albumIds
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onMembershipToggled(list.id, !isMember) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(list.title, color = VynlColors.TextPrimary, style = Body)
                    Checkbox(
                        checked = isMember,
                        onCheckedChange = { checked -> onMembershipToggled(list.id, checked) },
                        colors = CheckboxDefaults.colors(checkedColor = VynlColors.Accent)
                    )
                }
            }
        }
    }
}
