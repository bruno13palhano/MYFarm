package com.bruno13palhano.core.data.repository.item

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.ItemTableQueries
import com.bruno13palhano.core.data.repository.Data
import com.bruno13palhano.core.di.Dispatcher
import com.bruno13palhano.core.di.MYFarmDispatcher
import com.bruno13palhano.core.model.Item
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

internal class ItemLocalData @Inject constructor(
    private val itemData: ItemTableQueries,
    @Dispatcher(MYFarmDispatcher.IO) private val dispatcher: CoroutineDispatcher
): Data<Item> {
    override suspend fun insert(data: Item) {
        itemData.insert(
            indx = data.index.toLong(),
            x = data.x.toDouble(),
            y = data.y.toDouble(),
            radius = data.radius.toDouble(),
            successors = data.successorsIndices,
            name = data.name,
            description = data.description
        )
    }

    override suspend fun delete(id: Long) {
        itemData.delete(id = id)
    }

    override suspend fun update(data: Item) {
        itemData.update(
            indx = data.index.toLong(),
            x = data.x.toDouble(),
            y = data.y.toDouble(),
            radius = data.radius.toDouble(),
            successors = data.successorsIndices,
            name = data.name,
            description = data.description,
            id = data.id
        )
    }

    override fun getById(id: Long): Flow<Item> {
        return itemData.getById(id = id, mapper = ::mapToItem)
            .asFlow()
            .catch { it.printStackTrace() }
            .mapToOne(context = dispatcher)
    }

    override fun getAll(): Flow<List<Item>> {
        return itemData.getAll(mapper = ::mapToItem).asFlow().mapToList(context = dispatcher)
    }

    private fun mapToItem(
        id: Long,
        index: Long,
        x: Double,
        y: Double,
        radius: Double,
        successors: List<Int>,
        name: String,
        description: String
    ) = Item(
        id = id,
        index = index.toInt(),
        x = x.toFloat(),
        y = y.toFloat(),
        radius = radius.toFloat(),
        successorsIndices = successors,
        name = name,
        description = description
    )
}