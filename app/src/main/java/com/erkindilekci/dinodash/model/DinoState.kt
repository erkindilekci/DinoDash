package com.erkindilekci.dinodash.model

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import com.erkindilekci.dinodash.EARTH_Y_POSITION
import com.erkindilekci.dinodash.dinoPath
import com.erkindilekci.dinodash.dinoPath2

data class DinoState(
    var xPos: Float = 60f,
    var yPos: Float = EARTH_Y_POSITION,
    var velocityY: Float = 0f,
    var gravity: Float = 0f,
    var scale: Float = 0.4f,
    var keyframe: Int = 0,
    private var pathList: ArrayList<Path> = arrayListOf(),
    var isJumping: Boolean = false
) {
    val path: Path
        get() = if (keyframe <= 5) pathList[0] else pathList[1]

    init {
        pathList.add(dinoPath())
        pathList.add(dinoPath2())
    }

    fun init() {
        xPos = 60f
        yPos = EARTH_Y_POSITION
        velocityY = 0f
        gravity = 0f
        isJumping = false
    }

    fun move() {
        yPos += velocityY
        velocityY += gravity

        if (yPos > EARTH_Y_POSITION) {
            yPos = EARTH_Y_POSITION
            gravity = 0f
            velocityY = 0f
            isJumping = false
        }

        if (!isJumping) {
            changeKeyframe()
        }
    }

    fun jump() {
        if (yPos == EARTH_Y_POSITION) {
            isJumping = true
            velocityY = -40f
            gravity = 3f
        }
    }

    private fun changeKeyframe() {
        keyframe++
        if (keyframe == 10)
            keyframe = 0
    }

    fun getBounds(): Rect {
        return Rect(
            left = xPos,
            top = yPos - path.getBounds().height,
            right = xPos + path.getBounds().width,
            bottom = yPos
        )
    }
}
