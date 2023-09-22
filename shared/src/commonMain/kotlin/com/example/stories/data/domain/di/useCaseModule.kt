package com.example.stories.data.domain.di

import com.example.stories.data.domain.useCase.CommitChangesUseCase
import com.example.stories.data.domain.useCase.CreateBasicHistoryUseCase
import com.example.stories.data.domain.useCase.CreateEditingHistoryUseCase
import com.example.stories.data.domain.useCase.CreateImageElementUseCase
import com.example.stories.data.domain.useCase.CreateTextElementUseCase
import com.example.stories.data.domain.useCase.DeleteEditingHistoryUseCase
import com.example.stories.data.domain.useCase.DeleteElementUseCase
import com.example.stories.data.domain.useCase.DeleteHistoryUseCase
import com.example.stories.data.domain.useCase.GetAllStoriesUseCase
import com.example.stories.data.domain.useCase.GetEditingHistoryUseCase
import com.example.stories.data.domain.useCase.GetHistoryByIdUseCase
import com.example.stories.data.domain.useCase.SwapElementsUseCase
import com.example.stories.data.domain.useCase.UpdateHistoryDateRangeUseCase
import com.example.stories.data.domain.useCase.UpdateHistoryElementUseCase
import com.example.stories.data.domain.useCase.UpdateHistoryTitleUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single { CreateBasicHistoryUseCase(get()) }
    single { CreateEditingHistoryUseCase(get()) }
    single { CreateImageElementUseCase(get()) }
    single { GetEditingHistoryUseCase(get()) }
    single { CreateTextElementUseCase(get()) }
    single { DeleteEditingHistoryUseCase(get()) }
    single { DeleteElementUseCase(get()) }
    single { DeleteHistoryUseCase(get()) }
    single { GetAllStoriesUseCase(get()) }
    single { GetHistoryByIdUseCase(get()) }
    single { SwapElementsUseCase(get()) }
    single { UpdateHistoryDateRangeUseCase(get()) }
    single { UpdateHistoryElementUseCase(get()) }
    single { UpdateHistoryTitleUseCase(get()) }
    single { CommitChangesUseCase(get()) }
}
