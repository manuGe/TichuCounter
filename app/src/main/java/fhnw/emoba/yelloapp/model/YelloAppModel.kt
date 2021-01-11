package fhnw.emoba.yelloapp.model

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Gravity
import android.widget.Toast
import androidx.compose.runtime.*
import fhnw.emoba.yelloapp.data.Game
import fhnw.emoba.yelloapp.data.GameState
import fhnw.emoba.yelloapp.model.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class YelloAppModel(
    private val activity: Activity,
) {
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    var newGameDialog by mutableStateOf(false)

    var currentScreen by mutableStateOf(Screen.HOME)
    var currentGame: Game by mutableStateOf(Game(""))
    var isLoading by mutableStateOf(false)

    //    var gameList: MutableList<Game> = mutableListOf()
    var gameList: MutableList<Game> = mutableListOf(Game("Test"))

    var tempPointA by mutableStateOf(0)
    var tempPointB by mutableStateOf(0)
    var slider by mutableStateOf(50)
    var sliderWidth by mutableStateOf(0f)

    fun createGame(name: String) {
        val game = Game(name)
        currentGame = game
        gameList.add(game)
        currentScreen = Screen.GAME
    }

    fun submitPoints() {
        currentGame.points.add(Pair(tempPointA + slider, tempPointB + (100 - slider)))
        currentGame.time = System.currentTimeMillis()

        tempPointA = 0
        tempPointB = 0
        slider = 50

        val teamA = currentGame.points.map { it.first }
        val teamB = currentGame.points.map { it.second }

        currentGame.stats = teamA.sum().toString() + " - " + teamB.sum().toString()

        if (teamA.sum() >= 1000 || teamB.sum() >= 1000) {
            currentGame.state = GameState.FINISHED
        }
    }

    fun setSliderValue(value: Float) {
        val convertedValue = (value * (100 / sliderWidth)).roundToInt()
        val roundedValue = 5 * (convertedValue / 5)
        if (roundedValue < 0) {
            slider = 0
        } else if (roundedValue > 100) {
            slider = 100
        } else {
            slider = roundedValue
        }
    }

    fun moveSliderValue(value: Float) {
        var convertedDelta = (value * (100 / sliderWidth)).roundToInt()
        convertedDelta = 5 * (convertedDelta / 5)
        slider += convertedDelta
    }
}