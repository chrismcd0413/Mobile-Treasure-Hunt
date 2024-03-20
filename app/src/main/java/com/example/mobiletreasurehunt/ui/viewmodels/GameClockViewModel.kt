/*  Assignment 6

    Chris McDaniel / mcdachri@oregonstate.edu
    CS 492 / Oregon State University

 */

package com.example.mobiletreasurehunt.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameClockViewModel : ViewModel() {
    private val _state = MutableStateFlow<Long>(0L)
    val state: StateFlow<Long> = _state

    private var timerJob: Job? = null

    fun start() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while(true) {
                delay(1000)
                _state.value++
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
    }

    fun reset() {
        _state.value = 0
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    fun Long.formatTime(): String {
        val minutes = (this % 3600) / 60
        val seconds = this % 60
        return String.format("%02d : %02d", minutes, seconds)
    }
}