package com.example.stories.model.dataSource.di

import com.example.stories.model.dataSource.local.buildRealm
import com.example.stories.model.dataSource.local.history.HistoryLocalDataSourceImpl
import com.example.stories.model.dataSource.local.user.UserLocalDataSourceImpl
import com.example.stories.model.dataSource.remote.history.HistoryApi
import com.example.stories.model.dataSource.remote.buildKtorClient
import com.example.stories.model.dataSource.remote.user.UserApi
import com.example.stories.model.repository.dataSource.HistoryClaudDataSource
import com.example.stories.model.repository.dataSource.HistoryLocalDataSource
import com.example.stories.model.repository.dataSource.UserClaudDataSource
import com.example.stories.model.repository.dataSource.UserLocalDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single { buildRealm() }
    single { buildKtorClient() }
    single<HistoryLocalDataSource> { HistoryLocalDataSourceImpl(get()) }
    single<HistoryClaudDataSource> { HistoryApi(get()) }
    single<UserClaudDataSource> { UserApi(get()) }
    single<UserLocalDataSource> { UserLocalDataSourceImpl(get()) }
}
