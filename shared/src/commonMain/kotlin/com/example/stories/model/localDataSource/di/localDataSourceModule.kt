package com.example.stories.model.localDataSource.di

import com.example.stories.model.localDataSource.buildRealm
import com.example.stories.model.localDataSource.history.HistoryLocalDataSourceImpl
import com.example.stories.model.repository.dataSource.HistoryLocalDataSource
import org.koin.dsl.module

val localDataSourceModule = module {
    single { buildRealm() }
    single<HistoryLocalDataSource> { HistoryLocalDataSourceImpl(get()) }
}
