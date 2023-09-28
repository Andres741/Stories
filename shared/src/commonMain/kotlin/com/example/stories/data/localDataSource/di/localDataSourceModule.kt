package com.example.stories.data.localDataSource.di

import com.example.stories.data.localDataSource.buildRealm
import com.example.stories.data.localDataSource.history.HistoryLocalDataSource
import com.example.stories.data.localDataSource.history.HistoryLocalDataSourceImpl
import org.koin.dsl.module

val localDataSourceModule = module {
    single { buildRealm() }
    single<HistoryLocalDataSource> { HistoryLocalDataSourceImpl(get()) }
}
