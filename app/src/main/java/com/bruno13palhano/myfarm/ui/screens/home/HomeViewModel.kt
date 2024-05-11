package com.bruno13palhano.myfarm.ui.screens.home

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.item.ItemRepository
import com.bruno13palhano.core.di.DefaultItemRepository
import com.bruno13palhano.core.model.Item
import com.bruno13palhano.myfarm.ui.common.Vertex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @DefaultItemRepository private val itemRepository: ItemRepository
): ViewModel() {

    private var currentItem = Item(0L,0F,0F,0F,listOf(),"","")

    private var _vertices = MutableStateFlow<List<Vertex>>(listOf())
    val vertices = _vertices
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun getVertices() {
        viewModelScope.launch {
           itemRepository.getAll().collect {
               _vertices.value = it.map { item ->
                   Vertex(
                       id = item.id,
                       label = item.name,
                       center = Offset(x = item.x, y = item.y),
                       radius = item.radius,
                       edges = item.successorsIndices
                   )
               }
           }
        }
    }

    fun addEdge(startId: Long, endId: Long) {
        viewModelScope.launch {
            val successors = mutableListOf<Int>().apply {
                addAll(currentItem.successorsIndices)
            }
            successors.add(endId.toInt())

            itemRepository.update(
                data = currentItem.copy(
                    id = startId,
                    successorsIndices = successors
                )
            )
        }
    }

    fun getCurrentVertex(id: Long) {
        viewModelScope.launch {
            itemRepository.getById(id = id).collect {
                currentItem = it
            }
        }
    }

    fun updateVertex(
        id: Long,
        center: Offset,
        radius: Float,
        edges: List<Int>
    ) {
        viewModelScope.launch {
            itemRepository.update(
                data = currentItem.copy(
                    id = id,
                    x = center.x,
                    y = center.y,
                    radius = radius,
                    successorsIndices = edges
                )
            )
        }
    }

    fun addVertex(
        name: String,
        description: String,
        center: Offset = Offset(x = 100F, y = 100F),
        radius: Float = 50F
    ) {
        viewModelScope.launch {
            itemRepository.insert(
                data = Item(
                    id = 0L,
                    x = center.x,
                    y = center.y,
                    radius = radius,
                    successorsIndices = listOf(),
                    name = name,
                    description = description,
                )
            )
        }
    }
}