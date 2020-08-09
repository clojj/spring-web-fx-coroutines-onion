package com.example.onion.application.registration

import arrow.core.Either
import arrow.core.Right
import com.example.onion.domain.registration.UpdateUsernameUseCase
import com.example.onion.domain.registration.User
import com.example.onion.infrastructure.persistence.UserRepository
import com.example.onion.infrastructure.persistence.toDomain
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Mono
import java.util.*

@Configuration
class PostgresConfig {

    // TODO from properties
    @Bean
    fun postgresConnectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("localhost")
                        .port(5432)
                        .username("postgres")
                        .password("postgres")
                        .database("postgres")
                        .build())
    }

    @Bean
    fun reactiveTransactionManager(postgresConnectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(postgresConnectionFactory)
    }

    @Bean
    fun transactionalOperator(reactiveTransactionManager: ReactiveTransactionManager) =
            TransactionalOperator.create(reactiveTransactionManager)

}

@Configuration
class UseCaseConfiguration(
        private val userRepository: UserRepository
) {

    // DI happens here !

    @Bean
    fun readUserUseCase() = object : UpdateUsernameUseCase {

        override suspend fun updateUsername(id: UUID, name: String): Either<Throwable, Mono<User>> {
            return Either.catch {
                val modified = userRepository.findById(id)
                        .flatMap {
                            it.username = name
                            userRepository.save(it)
                        }
                modified.map { it.toDomain() }
            }
        }

        override suspend fun processUser(user: Mono<User>): Either<Throwable, Mono<User>> {
//            return Left(RuntimeException("processing error!"))
            return Right(user)
        }
    }
}