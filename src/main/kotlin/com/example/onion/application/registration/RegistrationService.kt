package com.example.onion.application.registration

import arrow.core.Either
import com.example.onion.domain.registration.UpdateUsernameUseCase
import com.example.onion.domain.registration.User
import org.springframework.stereotype.Service
import org.springframework.transaction.ReactiveTransaction
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import reactor.core.publisher.Mono
import java.util.*

/*
TransactionUsageException: Unsupported annotated transaction on suspending function detected:
RegistrationService.register(java.util.UUID,kotlin.coroutines.Continuation).
Use TransactionalOperator.transactional extensions instead.
*/

@Service
class RegistrationService(
        private val registrationUseCase: UpdateUsernameUseCase,
        private val transactionalOperator: TransactionalOperator
) {

    suspend fun update(id: UUID, name: String): Either<Throwable, Mono<User>> {
        return transactionalOperator.executeAndAwait { rtx: ReactiveTransaction ->

            val either = registrationUseCase.update(id, name)

            if (either.isLeft()) {
                rtx.setRollbackOnly()
            }
            either
        } as Either<Throwable, Mono<User>>
    }
}