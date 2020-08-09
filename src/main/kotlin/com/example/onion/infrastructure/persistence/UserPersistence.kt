package com.example.onion.infrastructure.persistence

import com.example.onion.domain.registration.User
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : ReactiveCrudRepository<UserData, UUID>

@Table("users")
data class UserData(
        @Id
        val id: UUID?,
        var email: String,
        var username: String
)

fun UserData.toDomain() = User(this.id, this.email, this.username)