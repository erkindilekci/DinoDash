package com.erkindilekci.dinodash.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameState(
    initialScore: Int = 0,
    initialHighScore: Int = 0,
    var isGameOver: Boolean = false
) {

    private val _currentScore: MutableStateFlow<Int> = MutableStateFlow(initialScore)
    val currentScore: StateFlow<Int> = _currentScore.asStateFlow()

    private val _highScore: MutableStateFlow<Int> = MutableStateFlow(initialHighScore)
    val highScore: StateFlow<Int> = _highScore.asStateFlow()

    fun increaseScore() {
        requireNotNull(_currentScore.value).inc()
        _currentScore.value = requireNotNull(_currentScore.value).inc()
    }

    fun replay() {
        val score = requireNotNull(_currentScore.value)
        val high = requireNotNull(_highScore.value)
        _highScore.value = if (score > high) score else high
        _currentScore.value = 0
        isGameOver = false
    }
}
