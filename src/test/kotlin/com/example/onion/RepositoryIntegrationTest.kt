package com.example.onion;

import com.example.onion.infrastructure.persistence.UserData
import com.example.onion.infrastructure.persistence.UserRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.annotation.Commit

@DisplayName("UserRepository Integration Tests")
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryIntegrationTest(@Autowired val userRepository: UserRepository) {

    @DisplayName("save user")
    @Test
    @Commit
    fun testSaveUser() {
        val userData = userRepository.save(UserData(null, "abc@def.com", "abc"))
        println(userData)
    }
}
