package com.bruno13palhano.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: MYFarmDispatcher)

enum class MYFarmDispatcher {
    IO,
    DEFAULT
}