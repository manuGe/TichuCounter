package com.manuel.tichucounter.model

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.manuel.tichucounter.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class TichuAppModel(activity: AppCompatActivity) : ViewModel() {
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    var newGameDialog by mutableStateOf(false)

    var currentScreen by mutableStateOf(Screen.HOME)
    var currentGame by mutableStateOf(Game(name = ""))
    var currentGameState by mutableStateOf(GameState.RUNNING)
    var currentGamePoints: MutableList<Pair<Int, Int>> = mutableStateListOf()

    var isLoading by mutableStateOf(false) //TODO: loading animation Homescreen
    var isDarkMode by mutableStateOf(false)

    var gameList: MutableList<Game> = mutableListOf()
//    TODO: LiveData/Flow needs a working Observer in the MainActivity
//    var gameList = gameRepository.getAllGames().asLiveData()
//    in GameDao:     fun getAll(): Flow<List<Game>>

    var tempPointA by mutableStateOf(0)
    var tempPointB by mutableStateOf(0)
    var slider by mutableStateOf(75)
    var sliderWidth by mutableStateOf(0f)

    private val preferenceRepository: PreferenceRepository
    private val gameRepository: GameRepository

    init {
        val db = AppDatabase.getInstance(activity.applicationContext)
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
        currentGame = Game(name = name)
        modelScope.launch {
            gameRepository.createGame(currentGame)

            // reset mutable values
            currentGamePoints = currentGame.points
            currentGameState = currentGame.state

            // reload new game list
            getAllGamesAsync()
        }
        currentScreen = Screen.GAME
    }

    fun submitPoints() {
        currentGamePoints.add(Pair(tempPointA + (slider - 25), tempPointB + (125 - slider)))
        currentGame.points = currentGamePoints
        currentGame.time = System.currentTimeMillis()

        tempPointA = 0
        tempPointB = 0
        slider = 75

        val teamA = currentGame.points.map { it.first }
        val teamB = currentGame.points.map { it.second }

        currentGame.stats = teamA.sum().toString() + " - " + teamB.sum().toString()

        if (teamA.sum() >= 1000 || teamB.sum() >= 1000) {
            currentGameState = GameState.FINISHED
            currentGame.state = currentGameState
        }

        updateGameAsync(currentGame)
    }

    fun updateGameAsync(game: Game) {
        modelScope.launch {
            gameRepository.setGame(game)
        }
    }

    fun getGameAsync(id: Long) {
        modelScope.launch {
            currentGame = gameRepository.getGame(id)

            currentGamePoints = mutableStateListOf()
            currentGame.points.forEach { pair ->
                currentGamePoints.add(pair)
            }
            currentGameState = currentGame.state
        }
    }

    fun getDarkModeAsync() {
        var counter = 0
        modelScope.launch {
            // check 5 seconds long if preferences were set
            while (preferenceRepository.countPreferences() <= 0 && counter < 50) {
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
        val convertedValue = (value * (150 / sliderWidth)).roundToInt()
        val roundedValue = 5 * (convertedValue / 5)
        if (roundedValue < 0) {
            slider = 0
        } else if (roundedValue > 150) {
            slider = 150
        } else {
            slider = roundedValue
        }
    }
}