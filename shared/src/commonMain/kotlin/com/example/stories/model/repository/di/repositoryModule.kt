package com.example.stories.model.repository.di

import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.repository.ImageRepository
import com.example.stories.model.domain.repository.UserRepository
import com.example.stories.model.repository.history.HistoryRepositoryImpl
import com.example.stories.model.repository.history.ImageRepositoryImpl
import com.example.stories.model.repository.history.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<HistoryRepository> { HistoryRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ImageRepository> { ImageRepositoryImpl(get()) }
}
