package com.example.stories.model.domain.di

import com.example.stories.model.domain.useCase.CommitHistoryChangesUseCase
import com.example.stories.model.domain.useCase.CreateBasicHistoryUseCase
import com.example.stories.model.domain.useCase.CreateEditingHistoryUseCase
import com.example.stories.model.domain.useCase.CreateImageElementUseCase
import com.example.stories.model.domain.useCase.CreateTextElementUseCase
import com.example.stories.model.domain.useCase.CreateUserUseCase
import com.example.stories.model.domain.useCase.DeleteEditingHistoryUseCase
import com.example.stories.model.domain.useCase.DeleteElementUseCase
import com.example.stories.model.domain.useCase.DeleteHistoryUseCase
import com.example.stories.model.domain.useCase.GetAllStoriesUseCase
import com.example.stories.model.domain.useCase.GetAllUsersUseCase
import com.example.stories.model.domain.useCase.GetClaudMockUseCase
import com.example.stories.model.domain.useCase.GetEditingHistoryUseCase
import com.example.stories.model.domain.useCase.GetHistoryByIdUseCase
import com.example.stories.model.domain.useCase.GetHistoryFromAPIUseCase
import com.example.stories.model.domain.useCase.GetLocalUserUseCase
import com.example.stories.model.domain.useCase.GetUserStoriesUseCase
import com.example.stories.model.domain.useCase.SwapElementsUseCase
import com.example.stories.model.domain.useCase.UpdateHistoryDateRangeUseCase
import com.example.stories.model.domain.useCase.UpdateHistoryElementUseCase
import com.example.stories.model.domain.useCase.UpdateHistoryTitleUseCase
import com.example.stories.model.domain.useCase.UpdateUserUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single { CreateBasicHistoryUseCase(get()) }
    single { CreateEditingHistoryUseCase(get()) }
    single { CreateImageElementUseCase(get()) }
    single { GetEditingHistoryUseCase(get()) }
    single { CreateTextElementUseCase(get()) }
    single { DeleteEditingHistoryUseCase(get()) }
    single { DeleteElementUseCase(get()) }
    single { DeleteHistoryUseCase(get(), get()) }
    single { GetAllStoriesUseCase(get()) }
    single { GetHistoryByIdUseCase(get()) }
    single { SwapElementsUseCase(get()) }
    single { UpdateHistoryDateRangeUseCase(get()) }
    single { UpdateHistoryElementUseCase(get()) }
    single { UpdateHistoryTitleUseCase(get()) }
    single { CommitHistoryChangesUseCase(get(), get()) }
    single { GetClaudMockUseCase(get()) }
    single { GetAllUsersUseCase(get()) }
    single { GetUserStoriesUseCase(get(), get()) }
    single { GetHistoryFromAPIUseCase(get()) }
    single { GetLocalUserUseCase(get()) }
    single { CreateUserUseCase(get(), get()) }
    single { UpdateUserUseCase(get()) }
}
