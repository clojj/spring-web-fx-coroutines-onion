package com.example.onion.application.registration

import com.example.onion.domain.registration.User
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
class RegistrationController(
        private val registrationService: RegistrationService
) {

    @PutMapping("/api/user/{id}")
    suspend fun updateUsername(@PathVariable id: String, @RequestParam name: String): ResponseEntity<Response> {

        val result = registrationService.update(UUID.fromString(id), name)

        // TODO: valid response HTTP 200 if any or all users not found ?
        return ResponseEntity.status(200).body(
                result.fold({
                    ReadUserError(it.message ?: it.toString())
                }) {
                    ReadUserResponse.fromDomain(it.awaitFirst())
                }
        )
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
