package com.example.onion.infrastructure.persistence

import com.example.onion.domain.User
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<UserData, String>

@Table("users")
data class UserData(
        @Id
        val id: UUID?,
        val email: String,
        val username: String
)

fun UserData.toDomain() = User(this.id, this.email, this.username)