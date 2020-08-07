package com.example.onion.application

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.example.onion.domain.User
import com.example.onion.infrastructure.persistence.UserData
import com.example.onion.infrastructure.persistence.UserRepository
import com.example.onion.infrastructure.persistence.toDomain
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class UserController(private val userRepository: UserRepository) {

    val readUserUseCase = object : ReadUserUseCase {
        override suspend fun readFromDB(id: UUID): Either<RuntimeException, User> {
            val optional: Optional<UserData> = userRepository.findById(id)
            return if (optional.isPresent) Right(optional.get().toDomain()) else Left(RuntimeException("not found"))
        }

        override suspend fun processUser(user: User): Either<Throwable, User> {
//                return Left(RuntimeException("processing error!"))
            return Right(user)
        }
    }

    @GetMapping("/api/user/{id}")
    fun read(@PathVariable id: UUID): ResponseEntity<Response> {

        // TODO create CoroutineScope
        val result = runBlocking {
            readUserUseCase.readUser(id)
        }

        return result.fold({
            ResponseEntity.status(400).body(ReadUserError(it.message ?: it.toString()))
        }) {
            ResponseEntity.status(200).body(ReadUserResponse(ReadUserResponseDto.fromDomain(it)))
        }
    }

}

data class ReadUserResponse(val responseDto: ReadUserResponseDto) : Response() {
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
data class RegisterUserError(val error: String) : Response()
data class ReadUserError(val error: String) : Response()
