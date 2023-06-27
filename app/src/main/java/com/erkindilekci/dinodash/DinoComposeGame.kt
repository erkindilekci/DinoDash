package com.erkindilekci.dinodash

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erkindilekci.dinodash.model.CactusState
import com.erkindilekci.dinodash.model.CloudState
import com.erkindilekci.dinodash.model.DinoState
import com.erkindilekci.dinodash.model.EarthState
import com.erkindilekci.dinodash.model.GameState
import com.erkindilekci.dinodash.ui.theme.cactusColor
import com.erkindilekci.dinodash.ui.theme.cloudColor
import com.erkindilekci.dinodash.ui.theme.currentScoreColor
import com.erkindilekci.dinodash.ui.theme.dinoColor
import com.erkindilekci.dinodash.ui.theme.earthColor
import com.erkindilekci.dinodash.ui.theme.gameOverColor
import com.erkindilekci.dinodash.ui.theme.highScoreColor

const val EARTH_Y_POSITION = 500f
private const val EARTH_GROUND_STROKE_WIDTH = 10f
private const val CLOUDS_SPEED = 1
private const val MAX_CLOUDS = 3
const val EARTH_OFFSET = 200
const val EARTH_SPEED = 9

var deviceWidthInPixels = 1920
var distanceBetweenCactus = 100

var showBounds = mutableStateOf(false)

@Composable
fun DinoGameScene(gameState: GameState) {
    val cloudsState by remember {
        mutableStateOf(
            CloudState(
                maxClouds = MAX_CLOUDS,
                speed = CLOUDS_SPEED
            )
        )
    }
    val earthState by remember { mutableStateOf(EarthState(maxBlocks = 2, speed = EARTH_SPEED)) }
    val cactusState by remember { mutableStateOf(CactusState(cactusSpeed = EARTH_SPEED)) }
    val dinoState by remember { mutableStateOf(DinoState()) }
    val currentScore by gameState.currentScore.collectAsState()
    val highScore by gameState.highScore.collectAsState()

    val earthColor = MaterialTheme.earthColor
    val cloudsColor = MaterialTheme.cloudColor
    val dinoColor = MaterialTheme.dinoColor
    val cactusColor = MaterialTheme.cactusColor

    if (!gameState.isGameOver) {
        gameState.increaseScore()
        cloudsState.moveForward()
        earthState.moveForward()
        cactusState.moveForward()
        dinoState.move()

        cactusState.cactusList.forEach {
            if (dinoState.getBounds()
                    .deflate(DOUBT_FACTOR)
                    .overlaps(
                        it.getBounds()
                            .deflate(DOUBT_FACTOR)
                    )
            ) {
                gameState.isGameOver = true
                return@forEach
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    if (!gameState.isGameOver)
                        dinoState.jump()
                    else {
                        cactusState.initCactus()
                        dinoState.init()
                        gameState.replay()
                    }
                }
            )
    ) {
        ShowBoundsSwitchView()
        HighScoreTextViews(currentScore, highScore)
        Canvas(modifier = Modifier.weight(1f)) {
            earthView(earthState, color = earthColor)
            cloudsView(cloudsState, color = cloudsColor)
            dinoView(dinoState, color = dinoColor)
            cactusView(cactusState, color = cactusColor)
        }
    }
    GameOverTextView(
        isGameOver = gameState.isGameOver,
        modifier = Modifier
            .padding(top = 150.dp)
            .fillMaxWidth()
    )

}

fun DrawScope.dinoView(dinoState: DinoState, color: Color) {
    withTransform({
        translate(
            left = dinoState.xPos,
            top = dinoState.yPos - dinoState.path.getBounds().height
        )
    }) {
        Log.w("Dino", "$dinoState.keyframe")
        drawPath(
            path = dinoState.path,
            color = color,
            style = Fill
        )
        drawBoundingBox(color = Color.Green, rect = dinoState.path.getBounds())
    }
}

fun DrawScope.cloudsView(cloudState: CloudState, color: Color) {
    cloudState.cloudsList.forEach { cloud ->
        withTransform({
            translate(
                left = cloud.xPos.toFloat(),
                top = cloud.yPos.toFloat()
            )
        })
        {
            drawPath(
                path = cloudState.cloudsList.first().path,
                color = color,
                style = Stroke(2f)
            )

            drawBoundingBox(color = Color.Blue, rect = cloud.path.getBounds())
        }
    }
}

fun DrawScope.earthView(earthState: EarthState, color: Color) {
    drawLine(
        color = color,
        start = Offset(x = 0f, y = EARTH_Y_POSITION),
        end = Offset(x = deviceWidthInPixels.toFloat(), y = EARTH_Y_POSITION),
        strokeWidth = EARTH_GROUND_STROKE_WIDTH
    )
    earthState.blocksList.forEach { block ->
        drawLine(
            color = color,
            start = Offset(x = block.xPos, y = EARTH_Y_POSITION + 20),
            end = Offset(x = block.size, y = EARTH_Y_POSITION + 20),
            strokeWidth = EARTH_GROUND_STROKE_WIDTH / 5,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 40f), 0f)
        )
        drawLine(
            color = color,
            start = Offset(x = block.xPos, y = EARTH_Y_POSITION + 30),
            end = Offset(x = block.size, y = EARTH_Y_POSITION + 30),
            strokeWidth = EARTH_GROUND_STROKE_WIDTH / 5,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 50f), 40f)
        )
    }
}

fun DrawScope.cactusView(cactusState: CactusState, color: Color) {
    cactusState.cactusList.forEach { cactus ->
        withTransform({
            scale(cactus.scale, cactus.scale)
            translate(
                left = cactus.xPos.toFloat(),
                top = cactus.getBounds().top * cactus.scale
            )
        })
        {
            drawPath(
                path = cactus.path,
                color = color,
                style = Fill
            )
            drawBoundingBox(color = Color.Red, rect = cactus.path.getBounds())
        }
    }
}

@Composable
fun HighScoreTextViews(currentScore: Int, highScore: Int) {
    Spacer(modifier = Modifier.padding(top = 50.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(text = "HI", style = TextStyle(color = MaterialTheme.highScoreColor))
        Spacer(modifier = Modifier.padding(start = 10.dp))
        Text(
            text = "$highScore".padStart(5, '0'),
            style = TextStyle(color = MaterialTheme.highScoreColor)
        )
        Spacer(modifier = Modifier.padding(start = 10.dp))
        Text(
            text = "$currentScore".padStart(5, '0'),
            style = TextStyle(color = MaterialTheme.currentScoreColor)
        )
    }
}

@Composable
fun ShowBoundsSwitchView() {
    Spacer(modifier = Modifier.padding(top = 20.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(text = "Show Bounds")
        Spacer(modifier = Modifier.padding(start = 10.dp))
        Switch(checked = showBounds.value, onCheckedChange = {
            showBounds.value = it
        })
    }
}

@Composable
fun GameOverTextView(
    modifier: Modifier = Modifier,
    isGameOver: Boolean = true,
) {
    Column(modifier = modifier) {
        Text(
            text = if (isGameOver) "GAME OVER" else "",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            letterSpacing = 5.sp,
            style = TextStyle(
                color = MaterialTheme.gameOverColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        if (isGameOver) {
            Icon(
                painter = painterResource(id = R.drawable.ic_replay),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 10.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}


fun DrawScope.drawBoundingBox(color: Color, rect: Rect, name: String? = null) {
    name?.let { Log.w("drawBounds", "$name $rect") }
    if (showBounds.value) {
        drawRect(color, rect.topLeft, rect.size, style = Stroke(3f))
        drawRect(
            color,
            rect.deflate(DOUBT_FACTOR).topLeft,
            rect.deflate(DOUBT_FACTOR).size,
            style = Stroke(
                width = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 4f), 0f)
            )
        )
    }
}

@Preview
@Composable
fun DinoGameScenePreview() {
    MaterialTheme {
        DinoGameScene(GameState())
    }
}
