package com.example.stoa

import com.example.stoa.configuration.JwtConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig::class)
class StoaApplication

fun main(args: Array<String>) {
    runApplication<StoaApplication>(*args)
}
