package fhnw.emoba.yelloapp.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import fhnw.emoba.yelloapp.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class YelloAppModel(applicationContext: Application) : ViewModel() {
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    var newGameDialog by mutableStateOf(false)

    var currentScreen by mutableStateOf(Screen.HOME)
    var currentGame: Game by mutableStateOf(Game(name = ""))
    var isLoading by mutableStateOf(false) //TODO: loading animation Homescreen
    var isDarkMode by mutableStateOf(false)

    var gameList: MutableList<Game> = mutableListOf()

    var tempPointA by mutableStateOf(0)
    var tempPointB by mutableStateOf(0)
    var slider by mutableStateOf(50)
    var sliderWidth by mutableStateOf(0f)

    private val preferenceRepository: PreferenceRepository
    private val gameRepository: GameRepository
    init {
        val db = AppDatabase.getInstance(applicationContext)
        val preferenceDao = db.preferenceDao()
        val gameDao = db.gameDao()
        preferenceRepository = PreferenceRepository(preferenceDao)
        gameRepository = GameRepository(gameDao)

        getAllGamesAsync()
        getDarkModeAsync()
    }

    fun getAllGamesAsync() {
        isLoading = true
        modelScope.launch {
            gameList = gameRepository.getAllGames()
            isLoading = false
        }
    }

    fun createGameAsync(name: String) {
        val game = Game(name = name)
        modelScope.launch {
            gameRepository.createGame(game)
            getAllGamesAsync()
        }
        currentGame = game
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

        updateGame(currentGame)
    }

    fun updateGame(game: Game) {
        modelScope.launch {
            gameRepository.setGame(game)
        }
    }

    fun getDarkModeAsync() {
        var counter = 0
        modelScope.launch {
            // check 5 seconds long if preferences were set
            while(preferenceRepository.countPreferences() <= 0 && counter < 50) {
                counter++
                Thread.sleep(100)
            }
            isDarkMode = preferenceRepository.getPreference("darkmode").value.toBoolean()
        }
    }

    fun setDarkModeValue(value: Boolean) {
        modelScope.launch {
            preferenceRepository.setPreference("darkmode", value)
            isDarkMode = value
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
}