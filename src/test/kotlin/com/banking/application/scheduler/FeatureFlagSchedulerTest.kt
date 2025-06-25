package com.banking.application.scheduler

import com.banking.application.exceptions.FeatureFlagPollingException
import com.banking.domain.dataccess.FeatureFlagDA
import com.banking.domain.entities.FeatureFlags
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FeatureFlagSchedulerTest {

  @Mock private lateinit var featureFlagDA: FeatureFlagDA

  private lateinit var featureFlagScheduler: FeatureFlagScheduler

  @BeforeEach
  fun setUp() {
    featureFlagScheduler = FeatureFlagScheduler(featureFlagDA)
  }

  @Test
  fun `should successfully poll feature flags`() {
    // Given
    val mockFeatureFlags =
            listOf(
                    FeatureFlags.builder()
                            .personId("user123")
                            .guarantee("guarantee1")
                            .key("ENABLE_EVENT_INGESTION_FLAG")
                            .value("true")
                            .build(),
                    FeatureFlags.builder()
                            .personId("user456")
                            .guarantee("guarantee2")
                            .key("ANOTHER_FLAG")
                            .value("false")
                            .build()
            )

    `when`(featureFlagDA.getFeatureFlag()).thenReturn(mockFeatureFlags)

    // When
    featureFlagScheduler.pollFeatureFlags()

    // Then
    verify(featureFlagDA, times(1)).getFeatureFlag()
  }

  @Test
  fun `should handle empty feature flags list`() {
    // Given
    val emptyFeatureFlags = emptyList<FeatureFlags>()
    `when`(featureFlagDA.getFeatureFlag()).thenReturn(emptyFeatureFlags)

    // When
    featureFlagScheduler.pollFeatureFlags()

    // Then
    verify(featureFlagDA, times(1)).getFeatureFlag()
  }

  @Test
  fun `should throw FeatureFlagPollingException when DA throws exception`() {
    // Given
    val originalException = RuntimeException("API Error")
    `when`(featureFlagDA.getFeatureFlag()).thenThrow(originalException)

    // When & Then
    val exception =
            assertThrows<FeatureFlagPollingException> { featureFlagScheduler.pollFeatureFlags() }

    assert(exception.message == "Failed to poll feature flags")
    assert(exception.cause == originalException)
  }

  @Test
  fun `should handle null feature flag values`() {
    // Given
    val mockFeatureFlags =
            listOf(
                    FeatureFlags.builder()
                            .personId("user123")
                            .guarantee("guarantee1")
                            .key("ENABLE_EVENT_INGESTION_FLAG")
                            .value(null)
                            .build()
            )

    `when`(featureFlagDA.getFeatureFlag()).thenReturn(mockFeatureFlags)

    // When
    featureFlagScheduler.pollFeatureFlags()

    // Then
    verify(featureFlagDA, times(1)).getFeatureFlag()
  }

  @Test
  fun `should handle large number of feature flags`() {
    // Given
    val largeFeatureFlagsList =
            (1..100).map { index ->
              FeatureFlags.builder()
                      .personId("user$index")
                      .guarantee("guarantee$index")
                      .key("FLAG_$index")
                      .value(if (index % 2 == 0) "true" else "false")
                      .build()
            }

    `when`(featureFlagDA.getFeatureFlag()).thenReturn(largeFeatureFlagsList)

    // When
    featureFlagScheduler.pollFeatureFlags()

    // Then
    verify(featureFlagDA, times(1)).getFeatureFlag()
  }

  @Test
  fun `should handle network timeout exception`() {
    // Given
    val timeoutException = RuntimeException("Connection timed out")
    `when`(featureFlagDA.getFeatureFlag()).thenThrow(timeoutException)

    // When & Then
    val exception =
            assertThrows<FeatureFlagPollingException> { featureFlagScheduler.pollFeatureFlags() }

    assert(exception.message == "Failed to poll feature flags")
    assert(exception.cause == timeoutException)
  }

  @Test
  fun `should handle JSON parsing exception from DA`() {
    // Given
    val jsonException = RuntimeException("Invalid JSON format")
    `when`(featureFlagDA.getFeatureFlag()).thenThrow(jsonException)

    // When & Then
    val exception =
            assertThrows<FeatureFlagPollingException> { featureFlagScheduler.pollFeatureFlags() }

    assert(exception.message == "Failed to poll feature flags")
    assert(exception.cause == jsonException)
  }
}
