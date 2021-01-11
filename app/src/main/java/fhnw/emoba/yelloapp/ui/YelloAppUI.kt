package fhnw.emoba.yelloapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fhnw.emoba.yelloapp.model.Screen
import fhnw.emoba.yelloapp.model.YelloAppModel
import fhnw.emoba.yelloapp.ui.screens.GameScreen
import fhnw.emoba.yelloapp.ui.screens.HomeScreen

@Composable
fun AppUI(model: YelloAppModel) {
    model.apply {
        val lightColors = lightColors(
            primary = Color(0xFF2A2B2E),
            primaryVariant = Color(0xFF3A3C40),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFF28B800),
            secondaryVariant = Color(0xFF1B7A00),
            onSecondary = Color(0xFF000000),
            background = Color(0xFFFFFFFF),
            onBackground = Color(0xFF000000),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF000000),
            error = Color(0xFFB00020),
            onError = Color(0xFFFFFFFF)
        )
        val darkColors = darkColors()
        val colors = if (enableDarkMode) darkColors else lightColors

        MaterialTheme(colors = colors) {
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
