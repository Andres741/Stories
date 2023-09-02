package com.example.stories

import com.example.stories.data.domain.di.useCasesModule
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.Module

expect fun getPlatformModule(): Module

fun initKoin() = startKoin {
    modules(
        getPlatformModule(),
        useCasesModule,
    )
}

object Component: KoinComponent
