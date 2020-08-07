package com.example.onion.infrastructure.persistence

import com.example.onion.domain.registration.User
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

// issue Structure101: won't show
@Repository
interface UserRepository : CrudRepository<UserData, UUID>

// issue Structure101: won't show
@Table("users")
data class UserData(
        @Id
        val id: UUID?,
        val email: String,
        val username: String
)

// issue Structure101: won't show
fun UserData.toDomain() = User(this.id, this.email, this.username)