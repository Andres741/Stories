package com.example.stories.model.domain.useCase

import com.example.stories.infrastructure.loading.LoadStatus
import com.example.stories.model.domain.model.History
import com.example.stories.model.domain.model.User
import com.example.stories.model.domain.repository.HistoryRepository
import com.example.stories.model.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetUserStoriesUseCase(
    private val userRepository: UserRepository,
    private val historyRepository: HistoryRepository,
) {

    suspend operator fun invoke(userId: String): LoadStatus<Pair<User, List<History>>> = coroutineScope {
        val userDef = async {
            userRepository.getUserById(userId)
        }

        val stories = historyRepository.getUserStories(userId)
        val user = userDef.await()

        val error = user.errorOrNull() ?: stories.errorOrNull()

        error?.let {
            return@coroutineScope LoadStatus.Error(it)
        }

        LoadStatus.Data(user.dataOrNull()!! to stories.dataOrNull()!!)
    }
}
