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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import com.bruno13palhano.myfarm.ui.common.Vertex
import kotlinx.coroutines.launch

@Composable
fun GraphSpace() {
    val vertices = remember { mutableStateListOf<Vertex>() }
    val edges: Array<Array<Int>> = remember { Array(20) { Array(20) { 0 } } }

    var touchIndex by remember { mutableIntStateOf(-1) }

    addVertex(vertices, Vertex(Offset(100F, 100F)))
    addVertex(vertices, Vertex(Offset(200F, 200F)))
    addVertex(vertices, Vertex(Offset(300F, 300F)))
    addVertex(vertices, Vertex(Offset(300F, 400F)))

    addEdge(0,1, edges)
    addEdge(0,2,edges)
    addEdge(2, 3, edges)

    val color = MaterialTheme.colorScheme.primary
    val scope = rememberCoroutineScope()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                scope.launch {
                    detectDragGestures(
                        onDragStart = { offset ->
                            touchIndex = -1
                            vertices.forEachIndexed { index, vertex ->
                                val isTouched = isTouched(vertex.center, offset, vertex.radius)

                                if (isTouched) {
                                    touchIndex = index
                                }
                            }
                        },
                        onDrag = { _, dragAmount: Offset ->
                            val vertex = vertices.getOrNull(touchIndex)

                            vertex?.let { vertexItem ->
                                val newPosition = vertexItem.center.plus(dragAmount)

                                vertices[touchIndex] = vertexItem.copy(
                                    center = newPosition,
                                    radius = 50F
                                )
                            }
                        },
                        onDragEnd = {
                            val vertex = vertices.getOrNull(touchIndex)

                            vertex?.let { vertexItem ->
                                vertices[touchIndex] = vertexItem.copy(
                                    radius = 40F
                                )
                            }
                        },
                        onDragCancel = {
                            val vertex = vertices.getOrNull(touchIndex)

                            vertex?.let { vertexItem ->
                                vertices[touchIndex] = vertexItem.copy(
                                    radius = 40F
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

                            vertices.forEachIndexed { index, vertex ->
                                isTouched = isTouched(vertex.center, offset, vertex.radius)

                                if (isTouched) {
                                    touchIndex = index
                                    println("Double Click")
                                }
                            }
                        },
                        onPress = {
                            awaitRelease()
                        }
                    )
                }
            }
    ) {
        vertices.forEachIndexed { index, vertex ->
            if (touchIndex != index) {
                drawCircle(
                    color = color,
                    center = vertex.center,
                    radius = vertex.radius
                )

                edges.forEachIndexed { indX, ints ->
                    ints.forEachIndexed { indY, i ->
                        if (i == 1) {
                            drawLine(
                                color = color,
                                start = vertices[indX].center,
                                end = vertices[indY].center
                            )
                        }
                    }
                }
            }
        }

        if (touchIndex > -1)
        vertices.getOrNull(touchIndex)?.let{ vertex ->
            drawCircle(
                color = color,
                center = vertex.center,
                radius = vertex.radius
            )

            edges.forEachIndexed { index, ints ->
                ints.forEachIndexed { ind, i ->
                    if (i == 1) {
                        drawLine(
                            color = color,
                            start = vertices[index].center,
                            end = vertices[ind].center
                        )
                    }
                }
            }
        }
    }
}

private fun addVertex(
    vertices: SnapshotStateList<Vertex>,
    vertex: Vertex
) {
    vertices.add(vertex)
}

private fun addEdge(
    start: Int,
    end: Int,
    edges: Array<Array<Int>>
) {
    edges[start][end] = 1
}

private fun isTouched(center: Offset, touchPosition: Offset, radius: Float): Boolean {
    return center.minus(touchPosition).getDistanceSquared() < (radius * radius * 4)
}