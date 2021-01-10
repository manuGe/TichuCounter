package fhnw.emoba.yelloapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.emoba.yelloapp.model.Screen
import fhnw.emoba.yelloapp.model.YelloAppModel
import fhnw.emoba.yelloapp.ui.screens.GameScreen
import fhnw.emoba.yelloapp.ui.screens.HomeScreen

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
