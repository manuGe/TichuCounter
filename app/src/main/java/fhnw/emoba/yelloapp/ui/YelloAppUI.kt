package fhnw.emoba.yelloapp.ui


import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import fhnw.emoba.thatsapp.ui.HomeScreen
import fhnw.emoba.yelloapp.model.Screen
import fhnw.emoba.yelloapp.model.YelloAppModel
import fhnw.emoba.yelloapp.ui.screens.GameScreen

@Composable
fun AppUI(model: YelloAppModel) {
    model.apply {
        MaterialTheme {
            Crossfade(current = currentScreen) { screen ->
                when (screen) {
                    Screen.HOME -> {
                        HomeScreen(model)
                    }
                    Screen.GAME -> {
                        GameScreen(model)
                    }
                }
            }
        }
    }
}

@Composable
fun VSpace(height: Int) {
    Spacer(modifier = Modifier.height(height.dp))
}

@Composable
fun HSpace(width: Int) {
    Spacer(modifier = Modifier.width(width.dp))
}
