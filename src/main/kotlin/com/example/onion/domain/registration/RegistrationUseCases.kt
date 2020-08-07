package com.example.onion.domain.registration

import arrow.core.Either
import arrow.core.computations.either
import arrow.fx.coroutines.evalOn
import kotlinx.coroutines.Dispatchers
import java.util.*

interface ReadUserUseCase {
    suspend fun readFromDB(id: UUID): Either<Throwable, User>
    suspend fun processUser(user: User): Either<Throwable, User>
}

// issue Structure101: won't show
suspend fun <R> R.readUser(id: UUID): Either<Throwable, User>
        where R : ReadUserUseCase =
        either {
            println("${System.currentTimeMillis()} readUser $id")
            val userFromDB = evalOn(Dispatchers.IO) { !readFromDB(id) }
            val user = evalOn(Dispatchers.Default) { !processUser(userFromDB) }
            user
        }

