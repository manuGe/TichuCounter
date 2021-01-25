package com.manuel.tichucounter.ui.screens

import androidx.compose.foundation.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manuel.tichucounter.data.GameState
import com.manuel.tichucounter.model.Screen
import com.manuel.tichucounter.model.TichuAppModel
import com.manuel.tichucounter.ui.BackButtonHandler
import com.manuel.tichucounter.ui.VSpace
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

@InternalCoroutinesApi
@Composable
fun GameScreen(model: TichuAppModel) {
    MaterialTheme {
        Scaffold(
            topBar = { GameTopBar(model) },
            bodyContent = { Body(model) }
        )
    }
    BackButtonHandler {
        model.currentScreen = Screen.HOME
        model.getAllGamesAsync()
    }
}

@Composable
fun GameTopBar(model: TichuAppModel) {
    model.apply {
        TopAppBar(
            title = { Text(text = Screen.GAME.title + ": " + currentGame.name) },
            navigationIcon = {
                IconButton(onClick = {
                    model.getAllGamesAsync()
                    currentScreen = Screen.HOME
                }) {
                    Icon(Icons.Filled.ArrowBack)
                }
            }
        )
    }
}

@InternalCoroutinesApi
@Composable
private fun Body(model: TichuAppModel) {
    with(model) {
        ScrollableColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        )
        {
            Table(model)
            if (currentGameState == GameState.RUNNING) {
                VSpace(10)
                PointPreview(model)
                Slider(model)
                VSpace(10)
                PointButtons(model)
                VSpace(10)
                SubmitAndResetButton(model)
            }
        }
        DeleteRoundDialog(model)
        UndoSnackbar(model)
    }
}

@Composable
fun Table(model: TichuAppModel) {
    model.apply {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Team A",
                    style = TextStyle(fontSize = 18.sp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Team B",
                    style = TextStyle(fontSize = 18.sp),
                    fontWeight = FontWeight.Bold
                )
            }
            VSpace(10)

            currentGamePoints.forEach { round ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable(onClick = {
                            deleteSelectedPoints = true
                            selectedPoints = round
                        }),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = round.first.toString(), style = TextStyle(fontSize = 17.sp))
                    Text(text = round.second.toString(), style = TextStyle(fontSize = 17.sp))
                }
            }
            Divider(
                modifier = Modifier.padding(start = 40.dp, end = 40.dp),
                color = MaterialTheme.colors.onBackground,
                thickness = 1.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val first = currentGamePoints.map { it.first }
                val second = currentGamePoints.map { it.second }
                Text(
                    text = first.sum().toString(),
                    style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = second.sum().toString(),
                    style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun PointPreview(model: TichuAppModel) {
    model.apply {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                modifier = Modifier
                    .height(80.dp)
                    .width(100.dp)
                    .padding(8.dp), elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$tempPointA",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(2.dp)
                    )
                    Text(
                        text = sliderTeamA.toString(),
                        textAlign = TextAlign.Center,
                        color = if (isSliderMoving) MaterialTheme.colors.secondaryVariant else Color.Gray,
                        modifier = Modifier
                            .padding(2.dp)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .height(80.dp)
                    .width(100.dp)
                    .padding(8.dp), elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$tempPointB",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(2.dp)
                    )
                    Text(
                        text = sliderTeamB.toString(),
                        textAlign = TextAlign.Center,
                        color = if (isSliderMoving) MaterialTheme.colors.secondaryVariant else Color.Gray,
                        modifier = Modifier
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Slider(model: TichuAppModel) {
    with(model) {
        val interactionState = remember { InteractionState() }
        Slider(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            value = slider.toFloat(),
            onValueChange = { setSliderValue(it) },
            valueRange = 0f..(sliderDefault * 2).toFloat(),
            steps = (sliderDefault * 2) / 5 - 1,
            interactionState = interactionState,
            thumbColor = MaterialTheme.colors.secondaryVariant,
            activeTrackColor = MaterialTheme.colors.secondaryVariant
        )
        isSliderMoving = when (interactionState.value.lastOrNull()) {
            Interaction.Focused -> false
            Interaction.Dragged -> true
            else -> false
        }
    }
}

@Composable
fun PointButtons(model: TichuAppModel) {
    model.apply {
        Column() {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant),
                    elevation = ButtonDefaults.elevation(5.dp),
                    onClick = { tempPointA += 100 }) {
                    Text(text = "+100", modifier = Modifier.padding(16.dp))
                }
                Button(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant),
                    elevation = ButtonDefaults.elevation(5.dp),
                    onClick = { tempPointB += 100 }) {
                    Text(text = "+100", modifier = Modifier.padding(16.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant),
                    elevation = ButtonDefaults.elevation(5.dp),
                    onClick = { tempPointA -= 100 }) {
                    Text(text = "-100", modifier = Modifier.padding(16.dp))
                }
                Button(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant),
                    elevation = ButtonDefaults.elevation(5.dp),
                    onClick = { tempPointB -= 100 }) {
                    Text(text = "-100", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun SubmitAndResetButton(model: TichuAppModel) {
    model.apply {
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.secondary
                ),
                elevation = ButtonDefaults.elevation(5.dp),
                onClick = { resetPoints() }) {
                Text(text = "Reset", modifier = Modifier.padding(16.dp))
            }
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                elevation = ButtonDefaults.elevation(5.dp),
                onClick = { submitPoints() }) {
                Text(text = "Submit", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@InternalCoroutinesApi
@Composable
fun DeleteRoundDialog(model: TichuAppModel) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    with(model) {
        if (deleteSelectedPoints) {
            AlertDialog(
                onDismissRequest = {
                    deleteSelectedPoints = false
                },
                title = {
                    Text(text = "Runde löschen")
                },
                text = {
                    Column() {
                        Text("Soll folgende Runde gelöscht werden?")
                        VSpace(20)
                        Text(
                            text = "Team A: ${selectedPoints.first}, Team B: ${selectedPoints.second}"
                        )
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                        onClick = {
                            deleteSelectedPoints()
                            deleteSelectedPoints = false
                            showSnackbar = true
                            hideSnackbar(5000)
                        }) {
                        Text("Löschen")
                    }
                },
                dismissButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                        onClick = {
                            deleteSelectedPoints = false
                        }) {
                        Text("Abbrechen")
                    }
                }
            )
        }
    }
}

@Composable
fun UndoSnackbar(model: TichuAppModel) {
    with(model) {
        if (showSnackbar) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Snackbar(
                    modifier = Modifier.padding(15.dp),
                    text = { Text(text = "Punkte wurden entfernt") },
                    action = {
                        TextButton(
                            onClick = {
                                restoreDeletedPoints()
                                showSnackbar = false
                            }) {
                            Text(
                                text = "Wiederherstellen",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.secondary
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}