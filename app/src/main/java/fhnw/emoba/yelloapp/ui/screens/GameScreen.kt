package fhnw.emoba.yelloapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.material.ButtonDefaults
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.dragGestureFilter
import androidx.compose.ui.gesture.pressIndicatorGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.emoba.yelloapp.data.GameState
import fhnw.emoba.yelloapp.model.Screen
import fhnw.emoba.yelloapp.model.YelloAppModel
import fhnw.emoba.yelloapp.ui.BackButtonHandler
import fhnw.emoba.yelloapp.ui.VSpace

@Composable
fun GameScreen(model: YelloAppModel) {
    MaterialTheme {
        Scaffold(
            topBar = { GameTopBar(model) },
            bodyContent = { Body(model) }
        )
    }
    BackButtonHandler {
        model.currentScreen = Screen.HOME
    }
}

@Composable
private fun Body(model: YelloAppModel) {
    with(model) {
        ScrollableColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        )
        {
            Table(model)
            VSpace(10)
            PointPreview(model)
            if (currentGame.state == GameState.RUNNING) {
                VSpace(10)
                Slider(model)
                VSpace(10)
                PointButtons(model)
                VSpace(10)
                SubmitAndResetButton(model)
            }
        }
    }
}

@Composable
fun Table(model: YelloAppModel) {
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

            currentGame.points.forEach { round ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
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
                val first = currentGame.points.map { it.first }
                val second = currentGame.points.map { it.second }
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
fun PointPreview(model: YelloAppModel) {
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
//                        style = TextStyle(fontSize = 18.sp),
                        modifier = Modifier
                            .padding(2.dp)
                    )
                    Text(
                        text = "$slider",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
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
//                        style = TextStyle(fontSize = 18.sp),
                        modifier = Modifier
                            .padding(2.dp)
                    )
                    Text(
                        text = (100 - slider).toString(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Slider(model: YelloAppModel) {
    with(model) {
        WithConstraints() {
            val density = AmbientDensity.current.density
            val canvasWidth = (constraints.maxWidth / density).dp
            val canvasHeight = 80.dp

            val delta = remember { mutableStateOf(0f) }
            var colors = MaterialTheme.colors

            Canvas(
                modifier = Modifier
                    .size(width = canvasWidth, height = canvasHeight)
                    .padding(20.dp)
                    .pressIndicatorGestureFilter(onStart = {
                        setSliderValue(it.x)
                    })
                    .dragGestureFilter(object : DragObserver {
                        override fun onStart(downPosition: Offset) {
                            delta.value = downPosition.x
                            super.onStart(downPosition)
                        }

                        override fun onDrag(dragDistance: Offset): Offset {
                            delta.value += dragDistance.x
                            setSliderValue(delta.value)
                            return super.onDrag(dragDistance)
                        }
                    }),
                onDraw = {
                    sliderWidth = size.width

                    drawRect(
                        color = Color(0xFF3A3C40),
                        topLeft = Offset(0f, 0f),
                        size = Size(sliderWidth, canvasHeight.value)
                    )
                    drawLine(
                        color = colors.secondary,
                        start = Offset(sliderWidth / 100 * slider, -20f),
                        end = Offset(sliderWidth / 100 * slider, canvasHeight.value + 20f),
                        strokeWidth = 20f
                    )
                })
        }
    }
}

@Composable
fun PointButtons(model: YelloAppModel) {
    model.apply {
        Column() {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    elevation = ButtonDefaults.elevation(5.dp),
                    onClick = { tempPointA += 100 }) {
                    Text(text = "+100", modifier = Modifier.padding(16.dp))
                }
                Button(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
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
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    elevation = ButtonDefaults.elevation(5.dp),
                    onClick = { tempPointA -= 100 }) {
                    Text(text = "-100", modifier = Modifier.padding(16.dp))
                }
                Button(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    elevation = ButtonDefaults.elevation(5.dp),
                    onClick = { tempPointB -= 100 }) {
                    Text(text = "-100", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun SubmitAndResetButton(model: YelloAppModel) {
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
                onClick = { tempPointA = 0; tempPointB = 0; slider = 50 }) {
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

