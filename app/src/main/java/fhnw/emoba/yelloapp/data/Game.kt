package fhnw.emoba.yelloapp.data

data class Game(val name: String) {
    val id = System.currentTimeMillis()
    val time: Long = id
    var stats = " - "
    var state = GameState.RUNNING
}

enum class GameState(val text: String) {
    RUNNING        ("läuft noch"),
    FINISHED       ("fertig"),
}