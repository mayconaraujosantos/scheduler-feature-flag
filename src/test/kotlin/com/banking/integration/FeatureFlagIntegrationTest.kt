package com.banking.integration

import com.banking.application.scheduler.FeatureFlagScheduler
import com.banking.domain.dataccess.FeatureFlagDA
import com.banking.domain.entities.FeatureFlags
import com.banking.domain.services.IntegrationBufferService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class FeatureFlagIntegrationTest {

  @Autowired private lateinit var featureFlagScheduler: FeatureFlagScheduler

  @Autowired private lateinit var integrationBufferService: IntegrationBufferService

  @MockBean private lateinit var featureFlagDA: FeatureFlagDA

  @Test
  fun `should complete feature flag polling flow successfully`() {
    // Given
    val mockFeatureFlags =
            listOf(
                    FeatureFlags.builder()
                            .personId("user123")
                            .guarantee("guarantee1")
                            .key("ENABLE_EVENT_INGESTION_FLAG")
                            .value("true")
                            .build()
            )

    org.mockito.Mockito.`when`(featureFlagDA.getFeatureFlag()).thenReturn(mockFeatureFlags)

    // When
    featureFlagScheduler.pollFeatureFlags()

    // Then
    org.mockito.Mockito.verify(featureFlagDA, org.mockito.Mockito.times(1)).getFeatureFlag()
  }

  @Test
  fun `should handle integration buffer service with feature flags`() {
    // Given
    val mockFeatureFlags =
            listOf(
                    FeatureFlags.builder()
                            .personId("user123")
                            .guarantee("guarantee1")
                            .key("ENABLE_EVENT_INGESTION_FLAG")
                            .value("true")
                            .build()
            )

    org.mockito.Mockito.`when`(featureFlagDA.getFeatureFlag()).thenReturn(mockFeatureFlags)

    val testEvent = "INTEGRATION_TEST_EVENT"

    // When
    integrationBufferService.bufferEvent(testEvent)

    // Then
    org.mockito.Mockito.verify(featureFlagDA, org.mockito.Mockito.times(1)).getFeatureFlag()
  }

  @Test
  fun `should handle disabled feature flag in integration flow`() {
    // Given
    val mockFeatureFlags =
            listOf(
                    FeatureFlags.builder()
                            .personId("user123")
                            .guarantee("guarantee1")
                            .key("ENABLE_EVENT_INGESTION_FLAG")
                            .value("false")
                            .build()
            )

    org.mockito.Mockito.`when`(featureFlagDA.getFeatureFlag()).thenReturn(mockFeatureFlags)

    val testEvent = "DISABLED_FLAG_TEST_EVENT"

    // When
    integrationBufferService.bufferEvent(testEvent)

    // Then
    org.mockito.Mockito.verify(featureFlagDA, org.mockito.Mockito.times(1)).getFeatureFlag()
  }
}
