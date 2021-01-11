package fhnw.emoba.yelloapp.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Game(val name: String) {
    val id = System.currentTimeMillis()
    var time by mutableStateOf(id)
    var stats = "0 - 0"
    var state by mutableStateOf(GameState.RUNNING)
    var points: MutableList<Pair<Int, Int>> = mutableStateListOf()
}

enum class GameState(val text: String) {
    RUNNING        ("läuft noch"),
    FINISHED       ("fertig"),
}