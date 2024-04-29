package com.bruno13palhano.core.di

import android.content.Context
import cache.ItemTableQueries
import com.bruno13palhano.cache.MYFarmDatabase
import com.bruno13palhano.core.data.database.DatabaseFactory
import com.bruno13palhano.core.data.database.DriverFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideMYFarmDatabaseFactoryDriver(
        @ApplicationContext context: Context
    ): MYFarmDatabase {
        return DatabaseFactory(
            driverFactory = DriverFactory(context = context)
        ).createDriver()
    }

    @Provides
    @Singleton
    fun provideItemTable(database: MYFarmDatabase): ItemTableQueries = database.itemTableQueries
}