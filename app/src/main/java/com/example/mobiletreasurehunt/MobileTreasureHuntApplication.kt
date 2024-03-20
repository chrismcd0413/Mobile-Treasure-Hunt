/*  Assignment 6

    Chris McDaniel / mcdachri@oregonstate.edu
    CS 492 / Oregon State University

 */

package com.example.mobiletreasurehunt

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobiletreasurehunt.data.Location
import com.example.mobiletreasurehunt.ui.components.CluePage
import com.example.mobiletreasurehunt.ui.components.FoundItScreen
import com.example.mobiletreasurehunt.ui.components.StartPage
import com.example.mobiletreasurehunt.ui.viewmodels.GameClockViewModel
import com.example.mobiletreasurehunt.ui.viewmodels.GameStateViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

enum class MobileTreasureHuntScreen() {
    Home, Clue, Found

}

@Composable
fun MobileTreasureHuntApp(
    gameClockViewModel: GameClockViewModel = viewModel(),
    gameViewModel: GameStateViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {


    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = MobileTreasureHuntScreen.valueOf(
        backStackEntry?.destination?.route ?: MobileTreasureHuntScreen.Home.name
    )

    val gameState by gameViewModel.gameState.collectAsState()

    val gameClock by gameClockViewModel.state.collectAsState()

    var showWrongLocationDialog by remember { mutableStateOf(false) }

    var showLoadingLocationIndicator by remember { mutableStateOf(false) }

    val activity = LocalContext.current as MainActivity
    activity.requestLocationPermission()

    val context = LocalContext.current
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }


    NavHost(
        navController = navController,
        startDestination = MobileTreasureHuntScreen.Home.name,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(route = MobileTreasureHuntScreen.Home.name) {
            StartPage(
                onStartButtonClicked = {
                    gameClockViewModel.start()
                    navController.navigate(MobileTreasureHuntScreen.Clue.name)
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        composable(route = MobileTreasureHuntScreen.Clue.name) {
            CluePage(
                onQuitDialogConfirm = {
                    navController.navigate(MobileTreasureHuntScreen.Home.name)
                    gameClockViewModel.reset()
                    gameViewModel.clearGameState()
                },
                onFoundItButtonClicked = {
                    showLoadingLocationIndicator = true
                    gameState.currentClue?.let {
                        checkLocation(it, locationClient, context) {
                            if (it) {
                                showWrongLocationDialog = false
                                showLoadingLocationIndicator = false
                                navController.navigate(MobileTreasureHuntScreen.Found.name)
                                gameClockViewModel.pause()
                                gameViewModel.advanceToNextClue()
                            } else {
                                showLoadingLocationIndicator = false
                                showWrongLocationDialog = true
                            }
                        }
                    } ?: quitGame(
                        { navController.navigate(MobileTreasureHuntScreen.Home.name) },
                        { gameClockViewModel.reset() },
                        { gameViewModel.clearGameState() }
                    )
                },
                onWrongLocationDialogDismissed = { showWrongLocationDialog = false },
                atWrongLocation = showWrongLocationDialog,
                gameState = gameState,
                showLoadingLocationIndicator = showLoadingLocationIndicator,
                gameClock = gameClock,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        composable(route = MobileTreasureHuntScreen.Found.name) {
            FoundItScreen(
                gameClock = gameClock,
                gameState = gameState,
                onClickNextClue = {
                    gameClockViewModel.start()
                    navController.navigate(MobileTreasureHuntScreen.Clue.name)
                },
                onGoHome = {
                    navController.navigate(MobileTreasureHuntScreen.Home.name)
                    gameClockViewModel.reset()
                    gameViewModel.clearGameState()

                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }

}

@SuppressLint("MissingPermission")
private fun checkLocation(
    clue: Location,
    locationServices: FusedLocationProviderClient,
    context: Context,
    callback: (Boolean) -> Unit
) {
    locationServices.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        CancellationTokenSource().token
    ).addOnSuccessListener {
        val earthRadiusKm: Double = 6372.8
        val clueLat = context.getString(clue.lat).toDouble()
        val clueLong = context.getString(clue.long).toDouble()
        val dLat = Math.toRadians(clueLat - it.latitude);
        val dLon = Math.toRadians(clueLong - it.longitude);
        val originLat = Math.toRadians(it.latitude);
        val destinationLat = Math.toRadians(clueLat);

        val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(Math.sin(dLon / 2), 2.toDouble()) * Math.cos(originLat) * Math.cos(destinationLat);
        val c = 2 * Math.asin(Math.sqrt(a));
        val distance = earthRadiusKm * c
        Log.d("LOC", "${distance}")
        Log.d("LOC", "Device Lat:${it.latitude.toString()} Device Long:${it.longitude.toString()}" )
        Log.d("LOC", "Loc Lat:${clueLat} Loc Long:${clueLong}" )
        callback(distance < 2)
    }
        .addOnFailureListener {
            callback(false)
        }
}

fun quitGame(
    navigate: () -> Unit,
    resetTimer: () -> Unit,
    resetGame: () -> Unit
) {
    navigate()
    resetTimer()
    resetGame()
}
