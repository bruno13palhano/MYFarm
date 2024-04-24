package com.bruno13palhano.myfarm.ui.common

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DrawProperties(
    val center: Offset,
    val radius: Float = 40F,
    val color: Color,
    val linesIndex: List<Int> = listOf()
)
