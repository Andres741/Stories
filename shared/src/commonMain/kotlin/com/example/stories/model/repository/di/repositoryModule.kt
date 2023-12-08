package com.example.stories.model.repository.di

import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.repository.history.HistoryRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }
}
