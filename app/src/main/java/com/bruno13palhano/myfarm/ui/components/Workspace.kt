package com.bruno13palhano.myfarm.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun Workspace() {
    Test()
}

@Preview
@Composable
private fun Test() {

    val unSelectedVertexColor = MaterialTheme.colorScheme.primary
    val selectedVertexColor = MaterialTheme.colorScheme.tertiary

    val drawList = remember { mutableStateListOf<DrawProperties>() }
    var touchIndex by remember { mutableIntStateOf(-1) }
    var size by remember { mutableStateOf(Size(0F, 0F)) }

    val scope = rememberCoroutineScope()

    drawList.add(
        DrawProperties(
            center = Offset(100F, 100F),
            color = unSelectedVertexColor,
            radius = 50F,
            lines = listOf(Pair(first = Offset(100F, 100F), second = Offset(400F, 400F)))
        )
    )
    drawList.add(
        DrawProperties(
            center = Offset(400F, 400F),
            color = unSelectedVertexColor,
            radius = 40F,
            lines = listOf(Pair(first = Offset(400F, 400F), second = Offset(600F, 600F)))
        )
    )
    drawList.add(
        DrawProperties(
            center = Offset(600F, 600F),
            color = unSelectedVertexColor,
            radius = 30F
        )
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        touchIndex = -1
                        drawList.forEachIndexed { index, drawProperties ->
                            val isTouched =
                                isTouched(drawProperties.center, offset, drawProperties.radius)

                            if (isTouched) {
                                touchIndex = index
                            }
                        }
                    },
                    onDrag = { change, dragAmount: Offset ->
                        val item = drawList.getOrNull(touchIndex)

                        item?.let { drawItem ->
                            val endPoint = drawItem.center.plus(dragAmount)

                            val points = mutableListOf<Pair<Offset, Offset>>()

                            drawList.forEach { drawProperties ->
                                points.add(Pair(drawProperties.center, endPoint))
                            }

                            drawList[touchIndex] = drawItem.copy(
                                center = endPoint,
                                color = selectedVertexColor,
                                lines = points
                            )
                        }
                    },
                    onDragEnd = {
                        val item = drawList.getOrNull(touchIndex)
                        item?.let { drawItem ->
                            drawList[touchIndex] = drawItem.copy(
                                color = unSelectedVertexColor
                            )
                        }
                    }
                )
            }
    ) {
        size = this.size

        drawList.forEachIndexed { index, drawProperties ->
            if (touchIndex != index) {
                drawCircle(
                    color = drawProperties.color,
                    center = drawProperties.center,
                    radius = drawProperties.radius
                )
            }
        }

        drawList.forEach { drawProperties ->
            if (drawProperties.lines.isNotEmpty() && touchIndex == -1) {
                drawProperties.lines.forEach { line ->
                    drawLine(
                        color = selectedVertexColor,
                        start = line.first,
                        end = line.second
                    )
                }
            }
        }

        if (touchIndex > -1) {
            drawList.getOrNull(touchIndex)?.let { drawProperties ->
                drawCircle(
                    color = drawProperties.color,
                    center = drawProperties.center,
                    radius = drawProperties.radius
                )
                if (drawProperties.lines.isNotEmpty()) {
                    drawProperties.lines.forEach { line ->
                        drawLine(
                            color = selectedVertexColor,
                            start = line.first,
                            end = line.second
                        )
                    }
                }
            }
        }
    }
}

private fun isTouched(center: Offset, touchPosition: Offset, radius: Float): Boolean {
    return center.minus(touchPosition).getDistanceSquared() < (radius * radius * 4)
}

data class DrawProperties(
    val center: Offset,
    val radius: Float = 50F,
    val color: Color,
    val lines: List<Pair<Offset, Offset>> = listOf()
)