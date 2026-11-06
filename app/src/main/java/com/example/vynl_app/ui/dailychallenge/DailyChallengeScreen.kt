package com.example.vynl_app.ui.dailychallenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vynl_app.data.challenge.LeaderboardEntry
import com.example.vynl_app.data.challenge.ChallengeType
import com.example.vynl_app.ui.theme.VynlColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyChallengeScreen(
    viewModel: DailyChallengeViewModel,
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    var tab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Daily Music Challenge",
                        color = VynlColors.TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "‹",
                            color = VynlColors.TextPrimary
                        )
                    }
                }
            )
        },
        containerColor = VynlColors.Background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .navigationBarsPadding()
        ) {

            TabRow(
                selectedTabIndex = tab
            ) {
                Tab(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    text = {
                        Text("Challenge")
                    }
                )

                Tab(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    text = {
                        Text("Leaderboard")
                    }
                )
            }

            if (tab == 0) {
                ChallengeContent(
                    state = state,
                    viewModel = viewModel
                )
            } else {
                LeaderboardContent(
                    entries = state.leaderboard
                )
            }
        }
    }
}

@Composable
private fun ChallengeContent(
    state: DailyChallengeUiState,
    viewModel: DailyChallengeViewModel
) {
    val challenge = state.challenge ?: return

    var showClue by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {

            Text(
                text = "MUSIC WORDLE",
                color = VynlColors.Accent,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = challenge.title,
                color = VynlColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${challenge.date} • ${challenge.type.name}",
                color = VynlColors.TextSecondary
            )
        }

        item {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = VynlColors.Surface
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        text = "Today's clue",
                        color = VynlColors.TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = challenge.clue,
                        color = VynlColors.TextReview
                    )

                    if (showClue) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "💡",
                                color = VynlColors.Accent
                            )

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Text(
                                text = challenge.metadataFragment,
                                color = VynlColors.TextSecondary
                            )
                        }
                    }

                    challenge.hints
                        .take(state.progress.hintsRevealed)
                        .forEachIndexed { index, hint ->

                            Text(
                                text = "Hint ${index + 1}: $hint",
                                color = VynlColors.TextSecondary
                            )
                        }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        OutlinedButton(
                            onClick = {
                                showClue = true
                            },
                            enabled = !showClue
                        ) {

                            Text("💡")

                            Spacer(
                                modifier = Modifier.width(6.dp)
                            )

                            Text("Metadata")
                        }

                        OutlinedButton(
                            onClick = viewModel::revealNextHint,
                            enabled =
                                !state.progress.completed &&
                                        state.progress.hintsRevealed < challenge.hints.size
                        ) {

                            Text("Next hint")
                        }
                    }
                }
            }
        }

        item {

            Text(
                text = "Attempts: ${state.progress.attempts}/${challenge.maxAttempts} • " +
                        "Hints: ${state.progress.hintsRevealed}/${challenge.hints.size} • " +
                        "Score: ${state.progress.score}",
                color = VynlColors.TextSecondary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(
                value = state.answer,
                onValueChange = viewModel::onAnswerChanged,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Album, artist or song")
                },
                enabled = !state.progress.completed,
                singleLine = true
            )
        }

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = viewModel::submitAnswer,
                    enabled =
                        !state.progress.completed &&
                                state.answer.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {

                    Text("Submit")
                }

                OutlinedButton(
                    onClick = viewModel::revealAnswer,
                    enabled = !state.progress.completed
                ) {

                    Text("Give up")
                }
            }
        }

        if (state.progress.completed) {

            item {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = VynlColors.SurfaceVariant
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text =
                                if (state.progress.solved) {
                                    "🎉 Correct answer!"
                                } else {
                                    "Challenge complete"
                                },
                            color = VynlColors.TextPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Answer: ${challenge.answer}",
                            color = VynlColors.Accent
                        )

                        Text(
                            text =
                                "Score: ${state.progress.score} • " +
                                        "Current streak: ${state.progress.streak} day(s)",
                            color = VynlColors.TextSecondary
                        )
                    }
                }
            }
        }

        if (!state.message.isNullOrBlank()) {

            item {

                Text(
                    text = state.message,
                    color = VynlColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun LeaderboardContent(
    entries: List<LeaderboardEntry>
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        item {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "🏆",
                    color = VynlColors.Accent
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Community leaderboard",
                    color = VynlColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(entries) { entry ->

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = VynlColors.Surface
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "#${entry.rank}  ${entry.username}",
                        color = VynlColors.TextPrimary
                    )

                    Text(
                        text =
                            "${entry.currentStreak} streak • " +
                                    "${entry.correctAnswers} correct",
                        color = VynlColors.TextSecondary
                    )
                }
            }
        }
    }
}