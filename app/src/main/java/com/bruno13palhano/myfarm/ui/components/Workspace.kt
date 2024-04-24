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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Workspace(drawPropertiesList: List<DrawProperties>) {
    InitWorkspace(drawPropertiesList = drawPropertiesList)
}

@Composable
private fun InitWorkspace(drawPropertiesList: List<DrawProperties>) {
    val unSelectedVertexColor = MaterialTheme.colorScheme.primary
    val selectedVertexColor = MaterialTheme.colorScheme.tertiary

    val drawList = remember { mutableStateListOf<DrawProperties>() }.apply {
        addAll(drawPropertiesList)
    }

    var touchIndex by remember { mutableIntStateOf(-1) }
    var size by remember { mutableStateOf(Size(0F, 0F)) }

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
                drawLines(
                    drawScope = this,
                    drawProperties = drawProperties,
                    drawList = drawList,
                    color = selectedVertexColor
                )
            }
        }

        if (touchIndex > -1) {
            drawList.getOrNull(touchIndex)?.let { drawProperties ->
                drawCircle(
                    color = drawProperties.color,
                    center = drawProperties.center,
                    radius = drawProperties.radius
                )
                drawLines(
                    drawScope = this,
                    drawProperties = drawProperties,
                    drawList = drawList,
                    color = selectedVertexColor
                )
            }
        }
    }
}

private fun isTouched(center: Offset, touchPosition: Offset, radius: Float): Boolean {
    return center.minus(touchPosition).getDistanceSquared() < (radius * radius * 4)
}

private fun drawLines(
    drawScope: DrawScope,
    drawProperties: DrawProperties,
    drawList: List<DrawProperties>,
    color: Color
) {
    if (drawProperties.linesIndex.isNotEmpty()) {
        drawProperties.linesIndex.forEach { lineIndex ->
            drawScope.drawLine(
                color = color,
                start = drawProperties.center,
                end = drawList[lineIndex].center
            )
        }
    }
}

data class DrawProperties(
    val center: Offset,
    val radius: Float = 40F,
    val color: Color,
    val linesIndex: List<Int> = listOf()
)