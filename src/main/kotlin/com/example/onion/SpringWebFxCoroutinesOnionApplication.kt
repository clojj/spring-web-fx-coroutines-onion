package com.example.onion

import com.example.onion.infrastructure.persistence.UserData
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories


@SpringBootApplication
@EnableJdbcRepositories
class SpringWebFxCoroutinesOnionApplication

fun main(args: Array<String>) {
    runApplication<SpringWebFxCoroutinesOnionApplication>(*args)
}
