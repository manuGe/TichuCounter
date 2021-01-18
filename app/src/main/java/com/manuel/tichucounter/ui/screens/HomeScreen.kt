package com.manuel.tichucounter.ui.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.AmbientLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manuel.tichucounter.TichuApp.model
import com.manuel.tichucounter.data.Game
import com.manuel.tichucounter.model.Screen
import com.manuel.tichucounter.model.TichuAppModel
import com.manuel.tichucounter.ui.BackButtonHandler
import com.manuel.tichucounter.ui.HSpace
import com.manuel.tichucounter.ui.VSpace
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(model: TichuAppModel) {
    val activity = (AmbientLifecycleOwner.current as ComponentActivity)
    val context = AmbientContext.current
    var lastBackPress = 0L
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    MaterialTheme {
        Scaffold(
            topBar = { HomeTopBar(model, scaffoldState) },
            bodyContent = { Body(model) },
            drawerContent = { Drawer(model) },
            scaffoldState = scaffoldState,
            floatingActionButton = { FAB(model) },
            floatingActionButtonPosition = FabPosition.End
        )
    }
    BackButtonHandler {
        if (System.currentTimeMillis() - lastBackPress > 1000) {
            Toast.makeText(context, "Drücke zweimal zum Schliessen", Toast.LENGTH_SHORT).show()
            lastBackPress = System.currentTimeMillis()
        } else {
            activity.finish()
        }
    }
}

@Composable
private fun Body(model: TichuAppModel) {
    model.apply {
        LazyColumn {
            items(gameList) { game ->
                GameCard(model, game)
            }
        }
    }
    NewGamePopup()
}

@Composable
fun HomeTopBar(model: TichuAppModel, scaffoldState: ScaffoldState) {
    model.apply {
        TopAppBar(
            title = { Text(Screen.HOME.title) },
            navigationIcon = {
                IconButton(onClick = { scaffoldState.drawerState.open() }) {
                    Icon(Icons.Filled.Menu)
                }
            }
        )
    }
}

@Composable
private fun Drawer(model: TichuAppModel) {
    with(model) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row {
                Text(text = "Dark Mode")
                HSpace(20)
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { setDarkModeValue(it) }
                )
            }
        }
    }
}

@Composable
private fun FAB(model: TichuAppModel) {
    model.apply {
        FloatingActionButton(
            onClick = { newGameDialog = true },
            content = { Icon(Icons.Filled.Add) })
    }
}

@Composable
private fun GameCard(model: TichuAppModel, game: Game) {
    with(model) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        getGameAsync(game.id)
                        currentScreen = Screen.GAME
                    }
                ),
            elevation = 8.dp
        ) {
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = game.name, style = TextStyle(fontSize = 20.sp))
                    VSpace(height = 5)
                    Text(text = "Zustand: " + game.state.text)
                    VSpace(height = 2)
                    Text(text = "Spielstand: " + game.stats)
                    VSpace(height = 2)
                    Text(
                        text = "Zuletzt gespielt: " + SimpleDateFormat("D.M.yy HH:mm", Locale.GERMAN).format(
                            Date(
                                game.time
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun NewGamePopup() {
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
                                    activeColor = MaterialTheme.colors.secondary,
                                    onValueChange = { textState.value = it }
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                                enabled = textState.value.text.isNotEmpty(),
                                onClick = {
                                    createGameAsync(textState.value.text)
                                    newGameDialog = false
                                }) {
                                Text("Bestätigen")
                            }
                        },
                        dismissButton = {
                            Button(
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
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
