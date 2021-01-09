package fhnw.emoba.thatsapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.emoba.yelloapp.YelloApp.model
import fhnw.emoba.yelloapp.data.Game
import fhnw.emoba.yelloapp.model.Screen
import fhnw.emoba.yelloapp.model.YelloAppModel
import fhnw.emoba.yelloapp.ui.HSpace
import fhnw.emoba.yelloapp.ui.VSpace
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(model: YelloAppModel) {
    MaterialTheme {
        Scaffold(
            topBar = { HomeTopBar(model) },
            bodyContent = { Body(model) },
            floatingActionButton = { FAB(model) },
            floatingActionButtonPosition = FabPosition.End
        )
    }
}

@Composable
private fun Body(model: YelloAppModel) {
    model.apply {
        LazyColumn {
            items(gameList) { game: Game ->
                GameCard(model, game)
            }
        }
        AlertDialogSample()
    }
}

@Composable
fun HomeTopBar(model: YelloAppModel) {
    model.apply {
        TopAppBar(
            title = { Text(Screen.HOME.title) }
        )
    }
}

@Composable
private fun FAB(model: YelloAppModel) {
    model.apply {
        FloatingActionButton(
//            onClick = { createGame() },
            onClick = { newGameDialog = true },
            content = { Icon(Icons.Filled.Add) })
    }
}

@Composable
private fun GameCard(model: YelloAppModel, game: Game) {
    Card(
        modifier = Modifier
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    model.currentGame = game
                    model.currentScreen = Screen.GAME
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = game.name, style = TextStyle(fontSize = 18.sp))
                VSpace(height = 5)
                Text(text = game.stats)
            }
        }
    }
}

@Composable
fun AlertDialogSample() {
    model.apply {
        val textState = remember { mutableStateOf(TextFieldValue()) }

        MaterialTheme {
            Column {
                if (newGameDialog) {

                    AlertDialog(
                        onDismissRequest = {
                            newGameDialog = false
                        },
                        title = {
                            Text(text = "Neues Spiel")
                        },
                        text = {
                            Column() {
                                Text("Wie soll das neue Spiel heissen?")
                                VSpace(8)
                                TextField(
                                    value = textState.value,
                                    onValueChange = { textState.value = it }
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    createGame(textState.value.text)
                                    newGameDialog = false
                                }) {
                                Text("Bestätigen")
                            }
                        },
                        dismissButton = {
                            Button(

                                onClick = {
                                    newGameDialog = false
                                }) {
                                Text("Abbrechen")
                            }
                        }
                    )
                }
            }

        }
    }
}

//TODO: Timestamp für zuletzt gespielt
@Composable
private fun Timestamp(game: Game) {
    Text(
        text = SimpleDateFormat("HH:mm").format(Date(game.time)),
        style = TextStyle(fontSize = 12.sp)
    )
}

