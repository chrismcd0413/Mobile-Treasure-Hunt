/*  Assignment 6

    Chris McDaniel / mcdachri@oregonstate.edu
    CS 492 / Oregon State University

 */

package com.example.mobiletreasurehunt.ui.components

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.mobiletreasurehunt.R
import com.example.mobiletreasurehunt.ui.viewmodels.GameState
import com.example.mobiletreasurehunt.ui.viewmodels.KonfettiViewModel
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.PartySystem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FoundItScreen(
    gameClock: Long,
    gameState: GameState,
    onClickNextClue: () -> Unit,
    onGoHome: () -> Unit,
    modifier: Modifier = Modifier,
    konfettiViewModel: KonfettiViewModel = KonfettiViewModel()
) {
    val konfettiState: KonfettiViewModel.State by konfettiViewModel.state.collectAsState()
    var header: String = ""
    if (gameState.currentClueIndex >= gameState.totalClues) {
        header = stringResource(R.string.treasure_hunt_complete_header)
        konfettiViewModel.congrats(
            listOf(
                MaterialTheme.colorScheme.primary.toArgb(),
                MaterialTheme.colorScheme.secondary.toArgb(),
                MaterialTheme.colorScheme.tertiary.toArgb()
            )
        )
    } else {
        header = stringResource(R.string.found_it_header)
    }
    Scaffold(
        floatingActionButton = {
            if (gameState.currentClueIndex < gameState.totalClues) {
                NextClueFAB(onClick = onClickNextClue)
            } else {
                GoHomeFAB(onClick = onGoHome)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { internalPadding ->
        when(konfettiState) {
            KonfettiViewModel.State.Idle -> {
                FoundItStructure(
                    gameClock = gameClock,
                    gameState = gameState,
                    header = header
                )
            }
            is KonfettiViewModel.State.Started -> {
                FoundItStructure(
                    gameClock = gameClock,
                    gameState = gameState,
                    header = header
                )
                KonfettiView(
                    parties = (konfettiState as KonfettiViewModel.State.Started).party,
                    modifier = Modifier.fillMaxSize(),
                    updateListener = object: OnParticleSystemUpdateListener {
                        override fun onParticleSystemEnded(
                            system: PartySystem,
                            activeSystems: Int
                        ) {
                            if (activeSystems == 0) konfettiViewModel.end()
                        }
                    }
                )

            }
        }
    }
}

@Composable
fun FoundItStructure(
    gameClock: Long,
    gameState: GameState,
    header: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
    ) {
        GameClock(gameClock)
        gameState.foundClue?.let {
            FoundItContent(
                header = header,
                locationName = stringResource(it.name),
                locationSummary = stringResource(it.summary),
                image = it.photo
            )
        }
    }
}
@Composable
fun FoundItContent(
    header: String,
    locationName: String,
    locationSummary: String,
    @DrawableRes image: Int,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Text(
            text = header,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            lineHeight = 1.29.em,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically))
        HeroImage(image)
        Text(
            text = locationName,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            lineHeight = 1.29.em,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically))
        Text(
            text = locationSummary,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            lineHeight = 1.43.em,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically))
    }
}

@Composable
fun NextClueFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        icon = { Icon(Icons.AutoMirrored.Filled.ArrowForward, "Next clue.") },
        text = {
            Text(
                stringResource(R.string.next_clue_button),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    )
}

@Composable
fun GoHomeFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        icon = { Icon(Icons.Filled.Home, "Go Home.") },
        text = {
            Text(
                stringResource(R.string.home_button),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    )
}

@Composable
fun HeroImage(
    @DrawableRes image: Int,
    modifier: Modifier = Modifier
) {
    val painter = painterResource(image)
    Image(
        painter = painter,
        contentDescription = "",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
    )
}

@Preview
@Composable
private fun FoundItPagePreview() {
    // FoundItScreen(Modifier)
}