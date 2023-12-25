package com.example.stories.model.domain.di

import com.example.stories.model.domain.useCase.CommitChangesUseCase
import com.example.stories.model.domain.useCase.CreateBasicHistoryUseCase
import com.example.stories.model.domain.useCase.CreateEditingHistoryUseCase
import com.example.stories.model.domain.useCase.CreateImageElementUseCase
import com.example.stories.model.domain.useCase.CreateTextElementUseCase
import com.example.stories.model.domain.useCase.DeleteEditingHistoryUseCase
import com.example.stories.model.domain.useCase.DeleteElementUseCase
import com.example.stories.model.domain.useCase.DeleteHistoryUseCase
import com.example.stories.model.domain.useCase.GetAllStoriesUseCase
import com.example.stories.model.domain.useCase.GetAllUsersUseCase
import com.example.stories.model.domain.useCase.GetClaudMockUseCase
import com.example.stories.model.domain.useCase.GetEditingHistoryUseCase
import com.example.stories.model.domain.useCase.GetHistoryByIdUseCase
import com.example.stories.model.domain.useCase.SwapElementsUseCase
import com.example.stories.model.domain.useCase.UpdateHistoryDateRangeUseCase
import com.example.stories.model.domain.useCase.UpdateHistoryElementUseCase
import com.example.stories.model.domain.useCase.UpdateHistoryTitleUseCase
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
    single { GetClaudMockUseCase(get()) }
    single { GetAllUsersUseCase(get()) }
}
