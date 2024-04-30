package com.bruno13palhano.core.data.repository.item

import com.bruno13palhano.core.data.repository.Data
import com.bruno13palhano.core.di.DefaultItemLocalData
import com.bruno13palhano.core.model.Item
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ItemRepositoryImpl @Inject constructor(
    @DefaultItemLocalData private val itemData: Data<Item>
) : ItemRepository {
    override suspend fun insert(data: Item) {
        itemData.insert(data = data)
    }

    override suspend fun delete(id: Long) {
        itemData.delete(id = id)
    }

    override fun getById(id: Long): Flow<Item> {
        return itemData.getById(id = id)
    }

    override fun getAll(): Flow<List<Item>> {
        return itemData.getAll()
    }
}