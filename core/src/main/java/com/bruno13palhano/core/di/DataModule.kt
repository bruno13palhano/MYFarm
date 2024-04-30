package com.bruno13palhano.core.di

import com.bruno13palhano.core.data.repository.Data
import com.bruno13palhano.core.data.repository.item.ItemLocalData
import com.bruno13palhano.core.model.Item
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
internal annotation class DefaultItemLocalData

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataModule {

    @DefaultItemLocalData
    @Singleton
    @Binds
    abstract fun bindDefaultItemLocalData(data: ItemLocalData): Data<Item>
}