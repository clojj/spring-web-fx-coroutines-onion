package com.example.onion.application.registration

import arrow.core.Either
import arrow.core.Right
import com.example.onion.domain.User
import com.example.onion.infrastructure.persistence.UserData
import com.example.onion.infrastructure.persistence.UserRepository
import com.example.onion.infrastructure.persistence.toDomain
import kotlinx.coroutines.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class UserController(private val userRepository: UserRepository) {

    val readUserUseCase = object : ReadUserUseCase {
        override suspend fun readFromDB(id: UUID): Either<Throwable, User> {
            return Either.catch {
                val optional: Optional<UserData> = userRepository.findById(id)
                optional.get().toDomain()
            }
        }

        override suspend fun processUser(user: User): Either<Throwable, User> {
//            return Left(RuntimeException("processing error!"))
            return Right(user)
        }
    }

    @GetMapping("/api/user/many")
    fun read(@RequestParam ids: String): ResponseEntity<out List<Response>> {

        val uuids = ids.split(",").map { UUID.fromString(it) }

        val result = runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            val deferreds = uuids.map {
                scope.async {
                    readUserUseCase.readUser(it)
                }
            }
            awaitAll(*deferreds.toTypedArray())
        }

        // TODO: valid response HTTP 200 if any or all users not found ?
        return ResponseEntity.status(200).body(result.map { either ->
            either.fold({
                ReadUserError(it.message ?: it.toString())
            }) {
                ReadUserResponse.fromDomain(it)
            }
        })
    }
}

data class ReadUserResponse(val user: ReadUserResponseDto) : Response() {
    companion object {
        fun fromDomain(user: User) = ReadUserResponse(ReadUserResponseDto.fromDomain(user))
    }
}

data class ReadUserResponseDto(
        val id: UUID?,
        val email: String,
        val username: String
) {
    companion object {
        fun fromDomain(user: User) = with(user) {
            ReadUserResponseDto(id = id, email = email, username = username)
        }
    }
}

sealed class Response
data class ReadUserError(val error: String) : Response()
