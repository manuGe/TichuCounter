package fhnw.emoba.yelloapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.runtime.onCommit
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.UriHandlerAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.emoba.yelloapp.model.Screen
import fhnw.emoba.yelloapp.model.YelloAppModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GameScreen(model: YelloAppModel) {
    MaterialTheme {
        Scaffold(
            topBar = { GameTopBar(model) },
            bodyContent = { Body(model) }
        )
    }
}

@Composable
private fun Body(model: YelloAppModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    )
    {
        Text(text = "body")
    }
}

@Composable
fun GameTopBar(model: YelloAppModel) {
    model.apply {
        TopAppBar(
            title = { Text(text = Screen.GAME.title + ": " + currentGame.name) },
            navigationIcon = {
                IconButton(onClick = { currentScreen = Screen.HOME }) {
                    Icon(Icons.Filled.ArrowBack)
                }
            }
        )
    }
}