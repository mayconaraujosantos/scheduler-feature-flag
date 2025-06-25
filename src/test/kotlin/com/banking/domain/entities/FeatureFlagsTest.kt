package com.banking.domain.entities

import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.junit.jupiter.api.Test

class FeatureFlagsTest {

  @Test
  fun `should create FeatureFlags with all parameters`() {
    // Given
    val personId = "user123"
    val guarantee = "guarantee1"
    val key = "ENABLE_EVENT_INGESTION_FLAG"
    val value = "true"

    // When
    val featureFlag =
            FeatureFlags(personId = personId, guarantee = guarantee, key = key, value = value)

    // Then
    assertEquals(personId, featureFlag.personId)
    assertEquals(guarantee, featureFlag.guarantee)
    assertEquals(key, featureFlag.key)
    assertEquals(value, featureFlag.value)
  }

  @Test
  fun `should create FeatureFlags with null optional parameters`() {
    // Given
    val key = "ENABLE_EVENT_INGESTION_FLAG"

    // When
    val featureFlag = FeatureFlags(personId = null, guarantee = null, key = key, value = null)

    // Then
    assertNull(featureFlag.personId)
    assertNull(featureFlag.guarantee)
    assertEquals(key, featureFlag.key)
    assertNull(featureFlag.value)
  }

  @Test
  fun `should create FeatureFlags using builder with all parameters`() {
    // Given
    val personId = "user123"
    val guarantee = "guarantee1"
    val key = "ENABLE_EVENT_INGESTION_FLAG"
    val value = "true"

    // When
    val featureFlag =
            FeatureFlags.builder()
                    .personId(personId)
                    .guarantee(guarantee)
                    .key(key)
                    .value(value)
                    .build()

    // Then
    assertEquals(personId, featureFlag.personId)
    assertEquals(guarantee, featureFlag.guarantee)
    assertEquals(key, featureFlag.key)
    assertEquals(value, featureFlag.value)
  }

  @Test
  fun `should create FeatureFlags using builder with only required parameters`() {
    // Given
    val key = "ENABLE_EVENT_INGESTION_FLAG"

    // When
    val featureFlag = FeatureFlags.builder().key(key).build()

    // Then
    assertNull(featureFlag.personId)
    assertNull(featureFlag.guarantee)
    assertEquals(key, featureFlag.key)
    assertNull(featureFlag.value)
  }

  @Test
  fun `should create FeatureFlags using builder with null values`() {
    // Given
    val key = "ENABLE_EVENT_INGESTION_FLAG"

    // When
    val featureFlag =
            FeatureFlags.builder().personId(null).guarantee(null).key(key).value(null).build()

    // Then
    assertNull(featureFlag.personId)
    assertNull(featureFlag.guarantee)
    assertEquals(key, featureFlag.key)
    assertNull(featureFlag.value)
  }

  @Test
  fun `should create FeatureFlags using builder with empty key`() {
    // Given
    val key = ""

    // When
    val featureFlag = FeatureFlags.builder().key(key).build()

    // Then
    assertEquals(key, featureFlag.key)
  }

  @Test
  fun `should create multiple FeatureFlags using builder`() {
    // Given
    val key1 = "FLAG_1"
    val key2 = "FLAG_2"

    // When
    val featureFlag1 =
            FeatureFlags.builder()
                    .personId("user1")
                    .guarantee("guarantee1")
                    .key(key1)
                    .value("true")
                    .build()

    val featureFlag2 =
            FeatureFlags.builder()
                    .personId("user2")
                    .guarantee("guarantee2")
                    .key(key2)
                    .value("false")
                    .build()

    // Then
    assertEquals("user1", featureFlag1.personId)
    assertEquals(key1, featureFlag1.key)
    assertEquals("true", featureFlag1.value)

    assertEquals("user2", featureFlag2.personId)
    assertEquals(key2, featureFlag2.key)
    assertEquals("false", featureFlag2.value)
  }

  @Test
  fun `should create FeatureFlags with boolean values`() {
    // Given
    val key = "ENABLE_FEATURE"

    // When
    val enabledFlag = FeatureFlags.builder().key(key).value("true").build()

    val disabledFlag = FeatureFlags.builder().key(key).value("false").build()

    // Then
    assertEquals("true", enabledFlag.value)
    assertEquals("false", disabledFlag.value)
  }

  @Test
  fun `should create FeatureFlags with special characters in values`() {
    // Given
    val key = "SPECIAL_FLAG"
    val specialValue = "value-with-special-chars!@#$%^&*()"

    // When
    val featureFlag = FeatureFlags.builder().key(key).value(specialValue).build()

    // Then
    assertEquals(specialValue, featureFlag.value)
  }
}
