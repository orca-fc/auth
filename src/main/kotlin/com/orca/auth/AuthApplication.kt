package com.orca.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class AuthApplication

suspend fun main(args: Array<String>) {
	runApplication<AuthApplication>(*args)
}
