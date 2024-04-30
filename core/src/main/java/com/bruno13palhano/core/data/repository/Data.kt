package com.bruno13palhano.core.data.repository

import kotlinx.coroutines.flow.Flow

interface Data<T> {
    suspend fun insert(data: T)
    suspend fun delete(id: Long)
    fun getById(id: Long): Flow<T>
    fun getAll(): Flow<List<T>>
}