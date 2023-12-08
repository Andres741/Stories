package com.example.stories.model.dataSource.di

import com.example.stories.model.dataSource.local.buildRealm
import com.example.stories.model.dataSource.local.history.HistoryLocalDataSourceImpl
import com.example.stories.model.dataSource.remote.history.HistoryApi
import com.example.stories.model.repository.dataSource.HistoryClaudDataSource
import com.example.stories.model.repository.dataSource.HistoryLocalDataSource
import org.koin.dsl.module

val localDataSourceModule = module {
    single { buildRealm() }
    single<HistoryLocalDataSource> { HistoryLocalDataSourceImpl(get()) }
    single<HistoryClaudDataSource> { HistoryApi() }
}
