package com.banking.application.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class FeatureFlagConfig(
    @Value("\${feature.flag.api.url}") val apiUrl: String,
    @Value("\${feature.flag.api.auth.token}") val authToken: String,
    @Value("\${feature.flag.ingestion.key}") val ingestionKey: String,
    @Value("\${feature.flag.api.timeout-ms:10000}") val apiTimout: Int,
    @Value("\${feature.flag.poll.interval-ms:10000}") val pollIntervalMs: Int
)
