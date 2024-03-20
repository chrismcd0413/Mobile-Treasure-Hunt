/*  Assignment 6

    Chris McDaniel / mcdachri@oregonstate.edu
    CS 492 / Oregon State University

 */

package com.example.mobiletreasurehunt.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mobiletreasurehunt.data.LocalDataProvider
import com.example.mobiletreasurehunt.data.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameStateViewModel : ViewModel() {

    private val allClues = LocalDataProvider.allLocations

    private val _gameState = MutableStateFlow(
        GameState(
            cluesRemaining = allClues,
            currentClue = allClues[0],
            currentClueIndex = 0,
            totalClues = allClues.size,
            foundClue = null
        )
    )
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun advanceToNextClue() {
        _gameState.update { currentState ->
            var nextLocation: Location? = null
            var index = currentState.currentClueIndex
            index++
            if (index < currentState.totalClues) {
                nextLocation = currentState.cluesRemaining[index]
            }
            currentState.copy(
                currentClue = nextLocation,
                currentClueIndex = index,
                foundClue = currentState.currentClue
            )
        }
    }

    fun clearGameState() {
        _gameState.update { currentState ->
            currentState.copy(
                cluesRemaining = allClues,
                currentClue = allClues[0],
                currentClueIndex = 0,
                totalClues = allClues.size,
                foundClue = null
            )
        }
    }
}

data class GameState (
    val cluesRemaining: List<Location> = listOf(),
    val currentClue: Location?,
    val foundClue: Location?,
    val currentClueIndex: Int = 0,
    val totalClues: Int = 0
)