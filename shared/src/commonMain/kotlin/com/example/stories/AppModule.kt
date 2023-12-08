package com.example.stories

import com.example.stories.model.domain.di.useCasesModule
import com.example.stories.model.localDataSource.di.localDataSourceModule
import com.example.stories.model.repository.di.repositoryModule
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.Module

expect fun getPlatformModule(): Module

fun initKoin() = startKoin {
    modules(
        getPlatformModule(),
        localDataSourceModule,
        repositoryModule,
        useCasesModule,
    )
}

object Component: KoinComponent
