package com.erkindilekci.dinodash.model

import androidx.compose.ui.graphics.Path
import com.erkindilekci.dinodash.cloudPath
import com.erkindilekci.dinodash.deviceWidthInPixels
import kotlin.random.Random

data class CloudState(
    val cloudsList: ArrayList<CloudModel> = arrayListOf<CloudModel>(),
    val maxClouds: Int = 3,
    val speed: Int = 1
) {
    init {
        initCloud()
    }

    private fun initCloud() {
        var startX = 150
        for (i in 0 until maxClouds) {
            val cloud = CloudModel(
                xPos = startX,
                yPos = rand(0, 100)
            )
            cloudsList.add(cloud)

            startX += rand(150, deviceWidthInPixels)
        }
    }

    fun moveForward() {
        for (i in 0 until maxClouds) {
            val cloud = cloudsList[i]
            cloud.xPos -= speed
            if (cloud.xPos < -100) {
                cloud.xPos = rand(deviceWidthInPixels, deviceWidthInPixels * rand(1, 2))
                cloud.yPos = rand(0, 100)
            }
        }
    }
}

data class CloudModel(
    var xPos: Int = 0,
    var yPos: Int = 0,
    var path: Path = cloudPath()
)

fun rand(start: Int, end: Int): Int {
    require(start <= end) { "Illegal Argument" }
    return (Math.random() * (end - start + 1)).toInt() + start
}

fun rand(start: Float, end: Float): Float {
    require(start <= end) { "Illegal Argument" }
    return Random(seed = System.currentTimeMillis()).nextFloat() * (end - start) + start
}
