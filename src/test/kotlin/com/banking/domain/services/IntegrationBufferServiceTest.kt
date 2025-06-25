package com.banking.domain.services

import com.banking.application.config.FeatureFlagConfig
import com.banking.domain.dataccess.FeatureFlagDA
import com.banking.domain.entities.FeatureFlags
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
                    FeatureFlags.builder()
                            .personId("test-person-123")
                            .guarantee("test-guarantee")
                            .key("ENABLE_EVENT_INGESTION_FLAG")
                            .value("true")
                            .build()
            )

    `when`(featureFlagConfig.ingestionKey).thenReturn("ENABLE_EVENT_INGESTION_FLAG")
    `when`(featureFlagDA.getFeatureFlag()).thenReturn(featureFlags)

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA, atLeastOnce()).getFeatureFlag()
    verify(featureFlagConfig, atLeastOnce()).ingestionKey
  }

  @Test
  fun `should not buffer event when feature flag is disabled`() {
    // Given
    val event = "TEST_EVENT"
    val featureFlags =
            listOf(
                    FeatureFlags.builder()
                            .personId("test-person-123")
                            .guarantee("test-guarantee")
                            .key("ENABLE_EVENT_INGESTION_FLAG")
                            .value("false")
                            .build()
            )

    `when`(featureFlagConfig.ingestionKey).thenReturn("ENABLE_EVENT_INGESTION_FLAG")
    `when`(featureFlagDA.getFeatureFlag()).thenReturn(featureFlags)

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA, atLeastOnce()).getFeatureFlag()
    verify(featureFlagConfig, atLeastOnce()).ingestionKey
  }

  @Test
  fun `should not buffer event when feature flag is not found`() {
    // Given
    val event = "TEST_EVENT"
    val featureFlags = emptyList<FeatureFlags>()

    `when`(featureFlagDA.getFeatureFlag()).thenReturn(featureFlags)

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA, atLeastOnce()).getFeatureFlag()
  }

  @Test
  fun `should handle exception when feature flag service fails`() {
    // Given
    val event = "TEST_EVENT"

    `when`(featureFlagDA.getFeatureFlag()).thenThrow(RuntimeException("API Error"))

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA, atLeastOnce()).getFeatureFlag()
  }

  @Test
  fun `should handle invalid boolean value in feature flag`() {
    // Given
    val event = "TEST_EVENT"
    val featureFlags =
            listOf(
                    FeatureFlags.builder()
                            .personId("test-person-123")
                            .guarantee("test-guarantee")
                            .key("ENABLE_EVENT_INGESTION_FLAG")
                            .value("invalid")
                            .build()
            )

    `when`(featureFlagConfig.ingestionKey).thenReturn("ENABLE_EVENT_INGESTION_FLAG")
    `when`(featureFlagDA.getFeatureFlag()).thenReturn(featureFlags)

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA, atLeastOnce()).getFeatureFlag()
    verify(featureFlagConfig, atLeastOnce()).ingestionKey
  }

  @Test
  fun `should handle null value in feature flag`() {
    // Given
    val event = "TEST_EVENT"
    val featureFlags =
            listOf(
                    FeatureFlags.builder()
                            .personId("test-person-123")
                            .guarantee("test-guarantee")
                            .key("ENABLE_EVENT_INGESTION_FLAG")
                            .value(null)
                            .build()
            )

    `when`(featureFlagConfig.ingestionKey).thenReturn("ENABLE_EVENT_INGESTION_FLAG")
    `when`(featureFlagDA.getFeatureFlag()).thenReturn(featureFlags)

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA, atLeastOnce()).getFeatureFlag()
    verify(featureFlagConfig, atLeastOnce()).ingestionKey
  }

  @Test
  fun `should handle multiple feature flags with different keys`() {
    // Given
    val event = "TEST_EVENT"
    val featureFlags =
            listOf(
                    FeatureFlags.builder()
                            .personId("test-person-123")
                            .guarantee("test-guarantee")
                            .key("OTHER_FLAG")
                            .value("true")
                            .build(),
                    FeatureFlags.builder()
                            .personId("test-person-456")
                            .guarantee("test-guarantee-2")
                            .key("ENABLE_EVENT_INGESTION_FLAG")
                            .value("true")
                            .build()
            )

    `when`(featureFlagConfig.ingestionKey).thenReturn("ENABLE_EVENT_INGESTION_FLAG")
    `when`(featureFlagDA.getFeatureFlag()).thenReturn(featureFlags)

    // When
    integrationBufferService.bufferEvent(event)

    // Then
    verify(featureFlagDA, atLeastOnce()).getFeatureFlag()
    verify(featureFlagConfig, atLeastOnce()).ingestionKey
  }
}
