package com.bruno13palhano.core.data.repository

import kotlinx.coroutines.flow.Flow

interface Data<T> {
    suspend fun insert(data: T)
    suspend fun delete(id: Long)
    suspend fun update(data: T)
    fun getById(id: Long): Flow<T>
    fun getAll(): Flow<List<T>>
}