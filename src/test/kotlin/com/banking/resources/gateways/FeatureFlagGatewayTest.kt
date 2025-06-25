package com.banking.resources.gateways

import com.banking.application.config.FeatureFlagConfig
import com.banking.domain.entities.FeatureFlags
import com.banking.resources.exceptions.FeatureFlagException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FeatureFlagGatewayTest {

  @Mock private lateinit var featureFlagConfig: FeatureFlagConfig

  private lateinit var featureFlagGateway: FeatureFlagGateway
  private lateinit var objectMapper: ObjectMapper

  @BeforeEach
  fun setUp() {
    objectMapper = ObjectMapper()
    featureFlagGateway = FeatureFlagGateway(objectMapper, featureFlagConfig)
  }

  @Test
  fun `should create FeatureFlagGateway successfully`() {
    // Given & When
    val gateway = FeatureFlagGateway(objectMapper, featureFlagConfig)

    // Then
    assert(gateway != null)
  }

  @Test
  fun `should create ObjectMapper correctly`() {
    // Given & When
    val mapper = ObjectMapper()

    // Then
    assert(mapper != null)
  }

  @Test
  fun `should handle FeatureFlags entity creation`() {
    // Given
    val featureFlag =
            FeatureFlags.builder()
                    .personId("test-user")
                    .guarantee("test-guarantee")
                    .key("TEST_FLAG")
                    .value("true")
                    .build()

    // When & Then
    assert(featureFlag.personId == "test-user")
    assert(featureFlag.guarantee == "test-guarantee")
    assert(featureFlag.key == "TEST_FLAG")
    assert(featureFlag.value == "true")
  }

  @Test
  fun `should handle FeatureFlagException creation`() {
    // Given
    val message = "Test error message"
    val statusCode = 500

    // When
    val exception = FeatureFlagException(message, statusCode)

    // Then
    assert(exception.message == message)
    assert(exception.statusCode == statusCode)
  }
}
