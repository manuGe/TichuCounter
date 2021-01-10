package fhnw.emoba.yelloapp.model

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Gravity
import android.widget.Toast
import androidx.compose.runtime.*
import fhnw.emoba.yelloapp.data.Game
import fhnw.emoba.yelloapp.model.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.properties.Delegates

class YelloAppModel(
    private val activity: Activity,
) {
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    var newGameDialog by mutableStateOf(false)
//    var textState by mutableStateOf("")

    var currentScreen by mutableStateOf(Screen.HOME)
    var currentGame: Game by mutableStateOf(Game(""))
    var isLoading by mutableStateOf(false)

    var gameList: MutableList<Game> = mutableListOf()


    fun createGame(name: String) {
        var game = Game(name)
        currentGame = game
        gameList.add(game)
        currentScreen = Screen.GAME
    }

    private fun Context.toast(message: String) {
        if (message.isNotBlank()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }
    }
}