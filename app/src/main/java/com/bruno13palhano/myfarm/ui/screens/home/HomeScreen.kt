package com.bruno13palhano.myfarm.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.myfarm.R
import com.bruno13palhano.myfarm.ui.common.DrawProperties
import com.bruno13palhano.myfarm.ui.common.Vertex
import com.bruno13palhano.myfarm.ui.components.MoreOptionsMenu
import com.bruno13palhano.myfarm.ui.components.Workspace

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val dList by viewModel.vertices.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getVertices()
    }

    HomeScreen(
        vertices = dList,
        onDragItemStart = viewModel::getCurrentVertex,
        onDragItemEnd = { id, drawProperties ->
            viewModel.updateVertex(
                id = id,
                center = drawProperties.vertex.center,
                radius = drawProperties.vertex.radius,
                edges = drawProperties.vertex.edges
            )
         },
        onMoreOptionsItemClick = { index ->
            when (index) {
                HomeOptions.ADD_VERTEX -> {}
                HomeOptions.DELETE_VERTEX -> {}
            }
        }
     )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    vertices: List<Vertex>,
    onDragItemStart: (id: Long) -> Unit,
    onDragItemEnd: (id: Long, drawProperties: DrawProperties) -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit
) {
    val items = arrayOf(
        stringResource(id = R.string.add_vertex_label),
        stringResource(id = R.string.delete_vertex_label)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.home_label)) },
                actions = {
                    var expanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(
                                id = R.string.more_options_menu_description
                            )
                        )

                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            MoreOptionsMenu(
                                items = items,
                                expanded = expanded,
                                onDismissRequest = { expanded = it },
                                onClick = onMoreOptionsItemClick
                            )
                        }
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            val unSelectedVertexColor = MaterialTheme.colorScheme.primary
            val drawList = vertices.map { vertex ->
                DrawProperties(vertex = vertex, color = unSelectedVertexColor)
            }

            Workspace(
                drawPropertiesList = drawList,
                onDragItemStart = onDragItemStart,
                onDragItemEnd = onDragItemEnd
            ) { size ->

            }
        }
    }
}

private object HomeOptions {
    const val ADD_VERTEX = 0
    const val DELETE_VERTEX = 1
}