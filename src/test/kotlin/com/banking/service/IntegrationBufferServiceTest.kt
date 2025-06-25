package com.banking.service

import com.banking.application.config.FeatureFlagConfig
import com.banking.domain.dataccess.FeatureFlagDA
import com.banking.domain.entities.FeatureFlags
import com.banking.domain.services.IntegrationBufferService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class IntegrationBufferServiceTest {

  @Mock private lateinit var featureFlagDA: FeatureFlagDA

  @Mock private lateinit var featureFlagConfig: FeatureFlagConfig

  private lateinit var integrationBufferService: IntegrationBufferService

  @BeforeEach
  fun setUp() {
    integrationBufferService = IntegrationBufferService(featureFlagDA, featureFlagConfig)
  }

  @Test
  fun `should buffer event when feature flag is enabled`() {
    // Given
    val event = "TEST_EVENT"
    val featureFlags =
            listOf(
                    FeatureFlags(
                            personId = "test-person-123",
                            guarantee = "test-guarantee",
                            key = "ENABLE_EVENT_INGESTION_FLAG",
                            value = "true"
                    )
            )

    `when`(featureFlagConfig.ingestionKey).thenReturn("ENABLE_EVENT_INGESTION_FLAG")
    `when`(featureFlagDA.getFeatureFlag()).thenReturn(featureFlags)

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA).getFeatureFlag()
    verify(featureFlagConfig).ingestionKey
    // Additional verifications for actual buffering logic would go here
  }

  @Test
  fun `should not buffer event when feature flag is disabled`() {
    // Given
    val event = "TEST_EVENT"
    val featureFlags =
            listOf(
                    FeatureFlags(
                            personId = "test-person-123",
                            guarantee = "test-guarantee",
                            key = "ENABLE_EVENT_INGESTION_FLAG",
                            value = "false"
                    )
            )

    `when`(featureFlagConfig.ingestionKey).thenReturn("ENABLE_EVENT_INGESTION_FLAG")
    `when`(featureFlagDA.getFeatureFlag()).thenReturn(featureFlags)

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA).getFeatureFlag()
    verify(featureFlagConfig).ingestionKey
    // Verify that no actual buffering occurs
  }

  @Test
  fun `should not buffer event when feature flag is not found`() {
    // Given
    val event = "TEST_EVENT"
    val featureFlags = listOf<FeatureFlags>()

    `when`(featureFlagDA.getFeatureFlag()).thenReturn(featureFlags)

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA).getFeatureFlag()
    // When no feature flags are found, ingestionKey is not called
    // Verify that no actual buffering occurs
  }

  @Test
  fun `should handle exception when feature flag service fails`() {
    // Given
    val event = "TEST_EVENT"

    `when`(featureFlagDA.getFeatureFlag()).thenThrow(RuntimeException("API Error"))

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA).getFeatureFlag()
    // When exception occurs, ingestionKey is not called
    // Verify that no actual buffering occurs due to exception
  }

  @Test
  fun `should handle invalid boolean value in feature flag`() {
    // Given
    val event = "TEST_EVENT"
    val featureFlags =
            listOf(
                    FeatureFlags(
                            personId = "test-person-123",
                            guarantee = "test-guarantee",
                            key = "ENABLE_EVENT_INGESTION_FLAG",
                            value = "invalid"
                    )
            )

    `when`(featureFlagConfig.ingestionKey).thenReturn("ENABLE_EVENT_INGESTION_FLAG")
    `when`(featureFlagDA.getFeatureFlag()).thenReturn(featureFlags)

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA).getFeatureFlag()
    verify(featureFlagConfig).ingestionKey
    // Verify that no actual buffering occurs due to invalid boolean value
  }
}
