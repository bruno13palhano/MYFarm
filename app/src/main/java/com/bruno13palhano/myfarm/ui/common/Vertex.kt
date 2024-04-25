package com.bruno13palhano.myfarm.ui.common

import androidx.compose.ui.geometry.Offset

data class Vertex(
    val center: Offset,
    val radius: Float = 40F,
    val linesIndex: List<Int> = listOf()
)
