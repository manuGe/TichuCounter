package fhnw.emoba.yelloapp.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "Game")
//data class Game(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int?,
//    @ColumnInfo(name = "game_time")
//    var time by mutableStateOf(id),
//    @ColumnInfo(name = "game_stats")
//    var stats = "0 - 0",
//    @ColumnInfo(name = "game_state")
//    var state: GameState = mutableStateOf(GameState.RUNNING)
//    @ColumnInfo(name = "game_points"),
//    var points: MutableList<Pair<Int, Int>> = mutableStateListOf()
//)

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