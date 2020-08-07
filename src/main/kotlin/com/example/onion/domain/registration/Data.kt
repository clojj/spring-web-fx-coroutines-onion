package com.example.onion.domain.registration

import java.util.*

data class User(
        val id: UUID?,
        val email: String,
        val username: String
)
