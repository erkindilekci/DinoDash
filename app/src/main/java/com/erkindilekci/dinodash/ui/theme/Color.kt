package com.erkindilekci.dinodash.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val MaterialTheme.earthColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFF535353) else Color(0xFFACACAC)

val MaterialTheme.cloudColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFFDBDBDB) else Color(0xFFACACAC)

val MaterialTheme.dinoColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFF535353) else Color(0xFFACACAC)

val MaterialTheme.cactusColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFF535353) else Color(0xFFACACAC)

val MaterialTheme.gameOverColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFFFFFFFF)

val MaterialTheme.currentScoreColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFF535353) else Color(0xFFACACAC)

val MaterialTheme.highScoreColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFF757575) else Color(0xFF909191)