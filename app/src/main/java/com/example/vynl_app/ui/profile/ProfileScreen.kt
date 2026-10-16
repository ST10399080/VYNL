@file:OptIn(
    androidx.compose.foundation.layout.ExperimentalLayoutApi::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class
)

package com.example.vynl_app.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vynl_app.data.profile.ThemePreference
import com.example.vynl_app.data.profile.UserProfile
import com.example.vynl_app.ui.theme.VynlColors

private val GenreOptions = listOf("Hip Hop", "R&B", "Pop", "Alternative", "Rock", "Jazz", "Afrobeats", "Electronic")
private val ArtistOptions = listOf("Drake", "Kendrick Lamar", "SZA", "Tyler, The Creator", "Beyoncé", "The Weeknd", "Tems", "Travis Scott")

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit = {},
    onDailyChallenge: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val profile = state.profile

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = VynlColors.TextPrimary) },
                navigationIcon = { IconButton(onClick = onBack) { Text("‹", color = VynlColors.TextPrimary) } },
                actions = {
                    IconButton(onClick = { if (state.isEditing) viewModel.cancelEditing() else viewModel.startEditing() }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit profile", tint = VynlColors.TextPrimary)
                    }
                }
            )
        },
        containerColor = VynlColors.Background
    ) { padding ->
        if (profile == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VynlColors.Accent)
            }
            return@Scaffold
        }

        if (state.isEditing) {
            EditProfileContent(
                profile = profile,
                isSaving = state.isSaving,
                modifier = Modifier.padding(padding),
                onSave = viewModel::saveProfile,
                onCancel = viewModel::cancelEditing
            )
        } else {
            ProfileContent(
                profile = profile,
                modifier = Modifier.padding(padding),
                onEdit = viewModel::startEditing,
                onDailyChallenge = onDailyChallenge
            )
        }
    }
}

@Composable
private fun ProfileContent(
    profile: UserProfile,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    onDailyChallenge: () -> Unit
) {
    Column(
        modifier.fillMaxSize().navigationBarsPadding().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileAvatar(profile.profilePictureUrl, 88.dp)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(profile.username, color = VynlColors.TextPrimary, fontWeight = FontWeight.Bold)
                Text(profile.biography, color = VynlColors.TextSecondary)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Filled.Edit, "Edit profile", tint = VynlColors.TextPrimary) }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ProfileStat(profile.reviewCount.toString(), "Reviews")
            ProfileStat(profile.ratingCount.toString(), "Ratings")
            ProfileStat(profile.followerCount.toString(), "Followers")
            ProfileStat(profile.listeningStreak.toString(), "Day streak")
        }

        Card(colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = VynlColors.Surface)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Favourite Genres", color = VynlColors.TextPrimary, fontWeight = FontWeight.Bold)
                ChipFlow(profile.favoriteGenres.ifEmpty { listOf("Add your genres") })
            }
        }

        Card(colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = VynlColors.Surface)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Favourite Artists", color = VynlColors.TextPrimary, fontWeight = FontWeight.Bold)
                ChipFlow(profile.favoriteArtists.ifEmpty { listOf("Add your artists") })
            }
        }

        Card(colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = VynlColors.Surface)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Theme Preference", color = VynlColors.TextPrimary, fontWeight = FontWeight.Bold)
                Text(profile.themePreference.name.lowercase().replaceFirstChar { it.uppercase() }, color = VynlColors.TextSecondary)
            }
        }

        Button(onClick = onDailyChallenge, modifier = Modifier.fillMaxWidth()) {
            Text("Play Daily Music Challenge")
        }
    }
}

@Composable
private fun EditProfileContent(
    profile: UserProfile,
    isSaving: Boolean,
    modifier: Modifier = Modifier,
    onSave: (String, String, String?, List<String>, List<String>, ThemePreference) -> Unit,
    onCancel: () -> Unit
) {
    var username by remember(profile) { mutableStateOf(profile.username) }
    var biography by remember(profile) { mutableStateOf(profile.biography) }
    var pictureUri by remember(profile) { mutableStateOf(profile.profilePictureUrl) }
    val selectedGenres = remember(profile) { mutableStateListOf<String>().apply { addAll(profile.favoriteGenres) } }
    val selectedArtists = remember(profile) { mutableStateListOf<String>().apply { addAll(profile.favoriteArtists) } }
    var theme by remember(profile) { mutableStateOf(profile.themePreference) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        pictureUri = uri?.toString() ?: pictureUri
    }

    Column(
        modifier.fillMaxSize().navigationBarsPadding().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileAvatar(pictureUri, 88.dp)
            Spacer(Modifier.width(14.dp))
            OutlinedButton(onClick = { imagePicker.launch("image/*") }) { Text("Change picture") }
        }

        OutlinedTextField(username, { username = it }, Modifier.fillMaxWidth(), label = { Text("Username") }, singleLine = true)
        OutlinedTextField(biography, { biography = it }, Modifier.fillMaxWidth(), label = { Text("Biography") }, minLines = 3, maxLines = 5)

        Text("Favourite Genres", color = VynlColors.TextPrimary, fontWeight = FontWeight.Bold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            GenreOptions.forEach { genre ->
                FilterChip(
                    selected = genre in selectedGenres,
                    onClick = { if (genre in selectedGenres) selectedGenres.remove(genre) else selectedGenres.add(genre) },
                    label = { Text(genre) }
                )
            }
        }

        Text("Favourite Artists", color = VynlColors.TextPrimary, fontWeight = FontWeight.Bold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ArtistOptions.forEach { artist ->
                FilterChip(
                    selected = artist in selectedArtists,
                    onClick = { if (artist in selectedArtists) selectedArtists.remove(artist) else selectedArtists.add(artist) },
                    label = { Text(artist) }
                )
            }
        }

        Text("Theme Preference", color = VynlColors.TextPrimary, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ThemePreference.entries.forEach { option ->
                FilterChip(selected = theme == option, onClick = { theme = option }, label = { Text(option.name.lowercase().replaceFirstChar { it.uppercase() }) })
            }
        }

        Spacer(Modifier.weight(1f))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = onCancel, Modifier.weight(1f), enabled = !isSaving) { Text("Cancel") }
            Button(
                onClick = { onSave(username, biography, pictureUri, selectedGenres.toList(), selectedArtists.toList(), theme) },
                Modifier.weight(1f), enabled = username.isNotBlank() && !isSaving
            ) {
                if (isSaving) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp) else {
                    Icon(Icons.Filled.Check, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Save")
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatar(url: String?, size: androidx.compose.ui.unit.Dp) {
    Box(Modifier.size(size).clip(CircleShape).background(VynlColors.SurfaceVariant), contentAlignment = Alignment.Center) {
        if (!url.isNullOrBlank()) {
            AsyncImage(model = url, contentDescription = "Profile picture", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        } else {
            Icon(Icons.Filled.Person, contentDescription = "Default profile picture", tint = VynlColors.TextSecondary, modifier = Modifier.size(size / 2))
        }
    }
}

@Composable
private fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = VynlColors.TextPrimary, fontWeight = FontWeight.Bold)
        Text(label, color = VynlColors.TextSecondary)
    }
}

@Composable
private fun ChipFlow(values: List<String>) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        values.forEach { AssistChip(onClick = {}, label = { Text(it) }) }
    }
}