package com.bruno13palhano.myfarm.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import com.bruno13palhano.myfarm.ui.common.DrawProperties
import com.bruno13palhano.myfarm.ui.common.Vertex
import kotlinx.coroutines.launch

@Composable
fun Workspace(
    drawPropertiesList: List<DrawProperties>,
    canvasSize: (size: Size) -> Unit
) {
    InitWorkspace(drawPropertiesList = drawPropertiesList, canvasSize = canvasSize)
}

@Composable
private fun InitWorkspace(
    drawPropertiesList: List<DrawProperties>,
    canvasSize: (size: Size) -> Unit
) {
    val unSelectedVertexColor = MaterialTheme.colorScheme.primary
    val selectedVertexColor = MaterialTheme.colorScheme.tertiary

    val drawList = remember { mutableStateListOf<DrawProperties>() }.apply {
        addAll(drawPropertiesList)
    }

    var touchIndex by remember { mutableIntStateOf(-1) }
    var size by remember { mutableStateOf(Size(0F, 0F)) }

    val scope = rememberCoroutineScope()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                scope.launch {
                    detectDragGestures(
                        onDragStart = { offset ->
                            touchIndex = -1
                            drawList.forEachIndexed { index, drawProperties ->
                                val isTouched =
                                    isTouched(drawProperties.vertex.center, offset, drawProperties.vertex.radius)

                                if (isTouched) {
                                    touchIndex = index
                                }
                            }
                        },
                        onDrag = { _, dragAmount: Offset ->
                            val item = drawList.getOrNull(touchIndex)

                            item?.let { drawItem ->
                                val endPoint = drawItem.vertex.center.plus(dragAmount)

                                drawList[touchIndex] = drawItem.copy(
                                    vertex = Vertex(center = endPoint, radius = 50F, linesIndex = drawItem.vertex.linesIndex),
                                    color = selectedVertexColor
                                )
                            }
                        },
                        onDragEnd = {
                            val item = drawList.getOrNull(touchIndex)
                            item?.let { drawItem ->
                                drawList[touchIndex] = drawItem.copy(
                                    vertex = Vertex(center = drawItem.vertex.center, radius = 40F, linesIndex = drawItem.vertex.linesIndex),
                                    color = unSelectedVertexColor
                                )
                            }
                        }
                    )
                }

                scope.launch {
                    detectTapGestures(
                        onDoubleTap = { offset ->
                            var isTouched: Boolean
                            touchIndex = -1
                            drawList.forEachIndexed { index, drawProperties ->
                                isTouched = isTouched(
                                    drawProperties.vertex.center,
                                    offset,
                                    drawProperties.vertex.radius
                                )

                                if (isTouched) {
                                    touchIndex = index
                                    println("Vertex click")
                                }
                            }
                        },
                        onLongPress = { offset ->
                            var isTouched: Boolean
                            touchIndex = -1

                            drawList.forEachIndexed { index, drawProperties ->
                                isTouched = isTouched(
                                    drawProperties.vertex.center,
                                    offset,
                                    drawProperties.vertex.radius
                                )

                                if (isTouched) {
                                    touchIndex = index
                                }
                            }
                            val item = drawList.getOrNull(touchIndex)

                            item?.let { drawItem ->
                                drawList[touchIndex] = drawItem.copy(
                                    vertex = Vertex(
                                        center = drawItem.vertex.center,
                                        radius = 50F,
                                        linesIndex = drawItem.vertex.linesIndex
                                    ),
                                    color = selectedVertexColor
                                )
                            }
                        },
                        onPress = {
                            awaitRelease()

                            val item = drawList.getOrNull(touchIndex)

                            item?.let { drawItem ->
                                drawList[touchIndex] = drawItem.copy(
                                    vertex = Vertex(
                                        center = drawItem.vertex.center,
                                        radius = 40F,
                                        linesIndex = drawItem.vertex.linesIndex
                                    ),
                                    color = unSelectedVertexColor
                                )
                            }
                        }
                    )
                }
            }
    ) {
        size = this.size
        canvasSize(size)

        drawList.forEachIndexed { index, drawProperties ->
            if (touchIndex != index) {
                drawCircle(
                    color = drawProperties.color,
                    center = drawProperties.vertex.center,
                    radius = drawProperties.vertex.radius
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
                    center = drawProperties.vertex.center,
                    radius = drawProperties.vertex.radius
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
    if (drawProperties.vertex.linesIndex.isNotEmpty()) {
        drawProperties.vertex.linesIndex.forEach { lineIndex ->
            drawScope.drawLine(
                color = color,
                start = drawProperties.vertex.center,
                end = drawList[lineIndex].vertex.center
            )
        }
    }
}