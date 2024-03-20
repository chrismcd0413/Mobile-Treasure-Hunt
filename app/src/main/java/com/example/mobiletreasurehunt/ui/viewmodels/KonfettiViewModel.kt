/*  Assignment 6

    Chris McDaniel / mcdachri@oregonstate.edu
    CS 492 / Oregon State University

 */

package com.example.mobiletreasurehunt.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class KonfettiViewModel : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    fun congrats(colors: List<Int>) {
//        val party = Party(
//            colors = listOf(
//                MaterialTheme.colorScheme.primary.toArgb(),
//                MaterialTheme.colorScheme.secondary.toArgb(),
//                MaterialTheme.colorScheme.tertiary.toArgb()
//            ),
//            emitter = Emitter(duration = 3000, TimeUnit.MILLISECONDS).max(3000)
//        )
        _state.value = State.Started(
            listOf(
                Party(
                    speed = 0f,
                    maxSpeed = 15f,
                    damping = 0.9f,
                    angle = Angle.BOTTOM,
                    spread = Spread.ROUND,
                    colors = colors,
                    emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(100),
                    position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0))
                )
            )
        )
    }

    fun end() {
        _state.value = State.Idle
    }

    sealed class State {
        class Started(val party: List<Party>) : State()

        object Idle: State()
    }
}