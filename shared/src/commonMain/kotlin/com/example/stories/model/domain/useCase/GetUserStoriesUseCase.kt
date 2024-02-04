package com.example.stories.model.domain.useCase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.stories.infrastructure.loading.LoadingError
import com.example.stories.infrastructure.loading.Response
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

    suspend operator fun invoke(userId: String): Response<Pair<User, List<History>>> = coroutineScope {
        val userDef = async {
            userRepository.getUserById(userId)
        }

        val stories = historyRepository.getUserStories(userId)
        val user = userDef.await()

        if (user is Either.Right && stories is Either.Right) {
            return@coroutineScope (user.value to stories.value).right()
        }

        val error = (user.leftOrNull() ?: stories.leftOrNull() ?: LoadingError.GenericError)
        error.left()
    }
}
