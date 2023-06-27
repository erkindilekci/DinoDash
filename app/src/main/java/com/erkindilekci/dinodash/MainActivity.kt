package com.erkindilekci.dinodash

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.erkindilekci.dinodash.model.GameState
import com.erkindilekci.dinodash.ui.theme.DinoDashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deviceMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(deviceMetrics)
        deviceWidthInPixels = deviceMetrics.widthPixels
        distanceBetweenCactus = (deviceWidthInPixels * 0.4f).toInt()

        setContent {
            DinoDashTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DinoGameScene(GameState())
                }
            }
        }
    }
}
