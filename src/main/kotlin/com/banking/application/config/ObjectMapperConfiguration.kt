package com.banking.application.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfiguration {

  @Bean("featureFlagObjectMapper")
  fun featureFlagObjectMapper(): ObjectMapper {
    return jacksonObjectMapper().apply {
      this.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
      this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      this.registerModule(JavaTimeModule())
    }
  }
}
