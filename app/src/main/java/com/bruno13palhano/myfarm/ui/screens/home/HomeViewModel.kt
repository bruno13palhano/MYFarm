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

    private var currentItem = Item(0L,0,0F,0F,0F,listOf(),"","")

    private var _dList = MutableStateFlow<List<Vertex>>(listOf())
    val dList = _dList
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun initG() {
        viewModelScope.launch {
           itemRepository.getAll().collect {
               _dList.value = it.map { item ->
                   println("Items: $it")
                   Vertex(
                       id = item.id,
                       center = Offset(x = item.x, y = item.y),
                       radius = item.radius,
                       edges = item.successorsIndices
                   )
               }
           }
        }
    }

    fun addEdge(start: Int, end: Int) {
        viewModelScope.launch {
            val successors = mutableListOf<Int>().apply {
                addAll(currentItem.successorsIndices)
            }
            successors.add(end)

            println("currentItem: ${currentItem.copy(
                id = start.toLong()+1,
                successorsIndices = successors,
            )}")

            itemRepository.update(
                data = currentItem.copy(
                    id = start.toLong()+1,
                    successorsIndices = successors
                )
            )
        }
    }

    fun getCurrentItem(id: Long) {
        viewModelScope.launch {
            itemRepository.getById(id = id).collect {
                currentItem = it
            }
        }
    }

    fun addVertex(
        name: String,
        description: String,
        index: Int,
        x: Float = 100F,
        y: Float = 100F,
        radius: Float = 40F
    ) {
        viewModelScope.launch {
            println("Item: ${Item(
                id = 0L,
                index = index,
                x = x,
                y = y,
                radius = radius,
                successorsIndices = listOf(),
                name = name,
                description = description,
            )}")
            itemRepository.insert(
                data = Item(
                    id = 0L,
                    index = index,
                    x = x,
                    y = y,
                    radius = radius,
                    successorsIndices = listOf(),
                    name = name,
                    description = description,
                )
            )
        }
    }
}