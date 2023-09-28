package com.example.stories.data.repository.di

import com.example.stories.data.repository.history.HistoryRepository
import com.example.stories.data.repository.history.HistoryRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }
}
