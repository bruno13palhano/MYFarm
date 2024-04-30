package com.bruno13palhano.core.di

import com.bruno13palhano.core.data.repository.item.ItemRepository
import com.bruno13palhano.core.data.repository.item.ItemRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class DefaultItemRepository

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

    @DefaultItemRepository
    @Singleton
    @Binds
    abstract fun bindDefaultItemRepository(repository: ItemRepositoryImpl): ItemRepository
}