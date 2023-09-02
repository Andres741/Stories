package com.example.stories.data.domain.di

import com.example.stories.data.domain.useCase.CreateBasicHistoryUseCase
import com.example.stories.data.domain.useCase.CreateEditingHistoryUseCase
import com.example.stories.data.domain.useCase.CreateImageElementUseCase
import com.example.stories.data.domain.useCase.CreateTextElementUseCase
import com.example.stories.data.domain.useCase.DeleteEditingHistoryUseCase
import com.example.stories.data.domain.useCase.DeleteElementUseCase
import com.example.stories.data.domain.useCase.DeleteHistoryUseCase
import com.example.stories.data.domain.useCase.EditHistoryUseCase
import com.example.stories.data.domain.useCase.GetAllStoriesUseCase
import com.example.stories.data.domain.useCase.GetHistoryByIdUseCase
import com.example.stories.data.domain.useCase.SwapElementsUseCase
import com.example.stories.data.domain.useCase.UpdateHistoryDateRangeUseCase
import com.example.stories.data.domain.useCase.UpdateHistoryElementUseCase
import com.example.stories.data.domain.useCase.UpdateHistoryTitleUseCase
import org.koin.dsl.module

val useCasesModule = module {
    single { CreateBasicHistoryUseCase() }
    single { CreateEditingHistoryUseCase() }
    single { CreateImageElementUseCase() }
    single { CreateTextElementUseCase() }
    single { DeleteEditingHistoryUseCase() }
    single { DeleteElementUseCase() }
    single { DeleteHistoryUseCase() }
    single { EditHistoryUseCase() }
    single { GetAllStoriesUseCase() }
    single { GetHistoryByIdUseCase() }
    single { SwapElementsUseCase() }
    single { UpdateHistoryDateRangeUseCase() }
    single { UpdateHistoryElementUseCase() }
    single { UpdateHistoryTitleUseCase() }
}
