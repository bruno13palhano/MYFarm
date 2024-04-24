package com.bruno13palhano.myfarm.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Workspace() {
    InitWorkspace()
}

@Preview
@Composable
private fun InitWorkspace() {

    val unSelectedVertexColor = MaterialTheme.colorScheme.primary
    val selectedVertexColor = MaterialTheme.colorScheme.tertiary

    val drawList = remember { mutableStateListOf<DrawProperties>() }
    var touchIndex by remember { mutableIntStateOf(-1) }
    var size by remember { mutableStateOf(Size(0F, 0F)) }

    drawList.add(
        DrawProperties(
            center = Offset(100F, 100F),
            color = unSelectedVertexColor,
            linesIndex = listOf(1)
        )
    )
    drawList.add(
        DrawProperties(
            center = Offset(400F, 400F),
            color = unSelectedVertexColor,
            linesIndex = listOf(2)
        )
    )
    drawList.add(
        DrawProperties(
            center = Offset(600F, 600F),
            color = unSelectedVertexColor,
        )
    )
    drawList.add(
        DrawProperties(
            center = Offset(400F, 800F),
            color = unSelectedVertexColor,
            linesIndex = listOf(1)
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
                    onDrag = { _, dragAmount: Offset ->
                        val item = drawList.getOrNull(touchIndex)

                        item?.let { drawItem ->
                            val endPoint = drawItem.center.plus(dragAmount)

                            drawList[touchIndex] = drawItem.copy(
                                center = endPoint,
                                radius = 50F,
                                color = selectedVertexColor
                            )
                        }
                    },
                    onDragEnd = {
                        val item = drawList.getOrNull(touchIndex)
                        item?.let { drawItem ->
                            drawList[touchIndex] = drawItem.copy(
                                color = unSelectedVertexColor,
                                radius = 40F
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

                if (drawProperties.linesIndex.isNotEmpty()) {
                    drawProperties.linesIndex.forEach { lineIndex ->
                        drawLine(
                            color = selectedVertexColor,
                            start = drawProperties.center,
                            end = drawList[lineIndex].center
                        )
                    }
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
                if (drawProperties.linesIndex.isNotEmpty()) {
                    drawProperties.linesIndex.forEach { lineIndex ->
                        drawLine(
                            color = selectedVertexColor,
                            start = drawProperties.center,
                            end = drawList[lineIndex].center
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
    val radius: Float = 40F,
    val color: Color,
    val linesIndex: List<Int> = listOf()
)