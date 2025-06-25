package com.banking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class FeatureFlagApplication

fun main(args: Array<String>) {
  runApplication<FeatureFlagApplication>(*args)
}
