package com.bruno13palhano.myfarm.ui.common

import androidx.compose.ui.geometry.Offset

data class Vertex(
    val id: Long = 0L,
    val label: String = "",
    val center: Offset,
    val radius: Float = 50F,
    var edges: List<Int> = listOf()
)
