package com.bruno13palhano.myfarm.ui.common

import androidx.compose.ui.geometry.Offset

data class Vertex(
    val id: Long = 0L,
    val center: Offset,
    val radius: Float = 40F,
    var edges: List<Int> = listOf()
)
