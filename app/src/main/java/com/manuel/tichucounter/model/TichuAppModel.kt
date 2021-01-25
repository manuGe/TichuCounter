package com.manuel.tichucounter.model

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.manuel.tichucounter.data.*
import kotlinx.coroutines.*
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class TichuAppModel(activity: AppCompatActivity) : ViewModel() {
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    var newGameDialog by mutableStateOf(false)
    var deleteSelectedPoints by mutableStateOf(false)
    var showSnackbar by mutableStateOf(false)
    var snackBarTimestamp by Delegates.notNull<Long>()
    lateinit var selectedPoints: Pair<Int, Int>

    var currentScreen by mutableStateOf(Screen.HOME)
    var currentGame by mutableStateOf(Game(name = ""))
    var currentGameState by mutableStateOf(GameState.RUNNING)
    var currentGamePoints: MutableList<Pair<Int, Int>> = mutableStateListOf()

    var isLoading by mutableStateOf(false) //TODO: loading animation Homescreen
    var isDarkMode by mutableStateOf(false)
    var isSliderMoving by mutableStateOf(false)

    var gameList: MutableList<Game> = mutableListOf()
//    TODO: LiveData/Flow needs a working Observer in the MainActivity
//    var gameList = gameRepository.getAllGames().asLiveData()
//    in GameDao:     fun getAll(): Flow<List<Game>>

    var tempPointA by mutableStateOf(0)
    var tempPointB by mutableStateOf(0)
    var sliderTeamA by mutableStateOf(0)
    var sliderTeamB by mutableStateOf(0)
    var sliderDefault = 80
    var slider by mutableStateOf(sliderDefault)
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

    fun deleteGameAsync(game: Game) {
        modelScope.launch {
            gameRepository.deleteGame(game)

            // reload new game list
            getAllGamesAsync()
        }
    }

    fun submitPoints() {
        currentGamePoints.add(Pair(tempPointA + sliderTeamA, tempPointB + sliderTeamB))
        currentGame.points = currentGamePoints
        currentGame.time = System.currentTimeMillis()

        resetPoints()

        val teamA = currentGame.points.map { it.first }
        val teamB = currentGame.points.map { it.second }

        currentGame.stats = teamA.sum().toString() + " - " + teamB.sum().toString()

        if (teamA.sum() >= 1000 || teamB.sum() >= 1000) {
            currentGameState = GameState.FINISHED
            currentGame.state = currentGameState
        }

        updateGameAsync(currentGame)
    }

    fun resetPoints() {
        tempPointA = 0
        tempPointB = 0
        sliderTeamA = 0
        sliderTeamB = 0
        slider = sliderDefault
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
        val maxSliderLength = sliderDefault * 2
        val steps = 5
        val roundedValue = steps * (value.roundToInt() / steps)
        if (roundedValue < 0) {
            slider = 0
        } else if (roundedValue > maxSliderLength) {
            slider = maxSliderLength
        } else {
            slider = roundedValue
        }

        if (slider == sliderDefault) {
            sliderTeamA = 0
            sliderTeamB = 0
        } else if (slider > sliderDefault) {
            sliderTeamA = 135 - slider
            sliderTeamB = slider - 35
        } else if (slider < sliderDefault) {
            sliderTeamA = 125 - slider
            sliderTeamB = slider - 25
        }
    }

    fun deleteSelectedPoints() {
        currentGamePoints.remove(selectedPoints)
        currentGame.points = currentGamePoints
        updateGameAsync(currentGame)
    }

    fun restoreDeletedPoints() {
        currentGamePoints.add(selectedPoints)
        currentGame.points = currentGamePoints
        updateGameAsync(currentGame)
    }

    @InternalCoroutinesApi
    fun hideSnackbar(delay: Long) {
        snackBarTimestamp = System.currentTimeMillis()
        modelScope.launch {
            delay(delay)
            if (System.currentTimeMillis() - snackBarTimestamp > delay) {
                showSnackbar = false
            }
        }
    }
}