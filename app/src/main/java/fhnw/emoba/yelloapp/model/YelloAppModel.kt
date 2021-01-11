package fhnw.emoba.yelloapp.model

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.input.key.Key.Companion.Settings
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import androidx.lifecycle.SavedStateHandle
import fhnw.emoba.yelloapp.data.Game
import fhnw.emoba.yelloapp.data.GameState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream
import javax.net.ssl.HttpsURLConnection
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class YelloAppModel(
    private val activity: Activity
) {
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

//    // with Preferences DataStore
//    val dataStore: DataStore<Preferences> = context.createDataStore(
//        name = "settings"
//    )
//
//    object SettingsSerializer : Serializer<Settings> {
//        override val defaultValue: Settings = Settings.getDefaultInstance()
//
//        override fun readFrom(input: InputStream): Settings {
//            try {
//                return Settings.parseFrom(input)
//            } catch (exception: InvalidProtocolBufferException) {
//                throw CorruptionException("Cannot read proto.", exception)
//            }
//        }
//
//        override fun writeTo(
//            t: Settings,
//            output: OutputStream) = t.writeTo(output)
//    }
//
//    val settingsDataStore: DataStore<Settings> = context.createDataStore(
//        fileName = "settings.pb",
//        serializer = SettingsSerializer
//    )
//
//    //Read from Store
//    val exampleCounterFlow: Flow<Int> = settingsDataStore.data
//        .map { settings ->
//            // The exampleCounter property is generated from the proto schema.
//            settings.exampleCounter
//        }
//
//    //Write to Store
//    suspend fun incrementCounter() {
//        settingsDataStore.updateData { currentSettings ->
//            currentSettings.toBuilder()
//                .setExampleCounter(currentSettings.exampleCounter + 1)
//                .build()
//        }
//    }

    var newGameDialog by mutableStateOf(false)

    var currentScreen by mutableStateOf(Screen.HOME)
    var currentGame: Game by mutableStateOf(Game(""))
    var isLoading by mutableStateOf(false)
    var enableDarkMode by mutableStateOf(false)

    var gameList: MutableList<Game> = mutableListOf()

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