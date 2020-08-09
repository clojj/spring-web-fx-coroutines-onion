package com.example.onion.domain.registration

import arrow.core.Either
import arrow.core.computations.either
import arrow.fx.coroutines.evalOn
import kotlinx.coroutines.Dispatchers
import reactor.core.publisher.Mono
import java.util.*

interface UpdateUsernameUseCase {
    suspend fun updateUsername(id: UUID, name: String): Either<Throwable, Mono<User>>
    suspend fun processUser(user: Mono<User>): Either<Throwable, Mono<User>>

    suspend fun update(id: UUID, name: String): Either<Throwable, Mono<User>> =
            either {
                evalOn(Dispatchers.IO) {
                    val userFromDB = !updateUsername(id, name)
                    val user = !processUser(userFromDB)
                    user
                }
            }
}
