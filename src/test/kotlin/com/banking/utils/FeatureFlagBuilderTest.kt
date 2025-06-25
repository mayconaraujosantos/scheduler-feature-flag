package com.banking.utils

import com.banking.domain.entities.FeatureFlags
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class FeatureFlagBuilderTest {

  @Test
  fun `should create feature flag with default values`() {
    // When
    val featureFlag = FeatureFlagBuilder.create()

    // Then
    assertEquals("person-123", featureFlag.personId)
    assertEquals("guarantee-456", featureFlag.guarantee)
    assertEquals("feature-flag-key", featureFlag.key)
    assertEquals("true", featureFlag.value)
  }

  @Test
  fun `should create feature flag with custom values`() {
    // Given
    val personId = "custom-person"
    val guarantee = "custom-guarantee"
    val key = "custom-key"
    val value = "false"

    // When
    val featureFlag =
            FeatureFlagBuilder.create(
                    personId = personId,
                    guarantee = guarantee,
                    key = key,
                    value = value
            )

    // Then
    assertEquals(personId, featureFlag.personId)
    assertEquals(guarantee, featureFlag.guarantee)
    assertEquals(key, featureFlag.key)
    assertEquals(value, featureFlag.value)
  }

  @Test
  fun `should create enabled flag`() {
    // When
    val featureFlag = FeatureFlagBuilder.createEnabledFlag("test-key")

    // Then
    assertEquals("test-key", featureFlag.key)
    assertEquals("true", featureFlag.value)
  }

  @Test
  fun `should create disabled flag`() {
    // When
    val featureFlag = FeatureFlagBuilder.createDisabledFlag("test-key")

    // Then
    assertEquals("test-key", featureFlag.key)
    assertEquals("false", featureFlag.value)
  }

  @Test
  fun `should create ingestion enabled flag`() {
    // When
    val featureFlag = FeatureFlagBuilder.createIngestionEnabledFlag("INGESTION_KEY")

    // Then
    assertEquals("INGESTION_KEY", featureFlag.key)
    assertEquals("true", featureFlag.value)
  }

  @Test
  fun `should create ingestion disabled flag`() {
    // When
    val featureFlag = FeatureFlagBuilder.createIngestionDisabledFlag("INGESTION_KEY")

    // Then
    assertEquals("INGESTION_KEY", featureFlag.key)
    assertEquals("false", featureFlag.value)
  }

  @Test
  fun `should create multiple flags`() {
    // When
    val flags = FeatureFlagBuilder.createMultipleFlags("INGESTION_KEY")

    // Then
    assertEquals(4, flags.size)
    assertEquals("INGESTION_KEY", flags[0].key)
    assertEquals("true", flags[0].value)
    assertEquals("other-feature-1", flags[1].key)
    assertEquals("true", flags[1].value)
    assertEquals("other-feature-2", flags[2].key)
    assertEquals("false", flags[2].value)
    assertEquals("optional-feature", flags[3].key)
    assertNull(flags[3].value)
  }

  @Test
  fun `should create list from vararg flags`() {
    // Given
    val flag1 = FeatureFlags.builder().key("flag1").build()
    val flag2 = FeatureFlags.builder().key("flag2").build()

    // When
    val flags = FeatureFlagBuilder.createList(flag1, flag2)

    // Then
    assertEquals(2, flags.size)
    assertEquals("flag1", flags[0].key)
    assertEquals("flag2", flags[1].key)
  }

  @Test
  fun `should create enabled flags list`() {
    // Given
    val keys = listOf("flag1", "flag2", "flag3")

    // When
    val flags = FeatureFlagBuilder.createEnabledFlags(keys)

    // Then
    assertEquals(3, flags.size)
    flags.forEach { flag -> assertEquals("true", flag.value) }
    assertEquals("flag1", flags[0].key)
    assertEquals("flag2", flags[1].key)
    assertEquals("flag3", flags[2].key)
  }

  @Test
  fun `should create disabled flags list`() {
    // Given
    val keys = listOf("flag1", "flag2")

    // When
    val flags = FeatureFlagBuilder.createDisabledFlags(keys)

    // Then
    assertEquals(2, flags.size)
    flags.forEach { flag -> assertEquals("false", flag.value) }
    assertEquals("flag1", flags[0].key)
    assertEquals("flag2", flags[1].key)
  }

  @Test
  fun `should create empty list`() {
    // When
    val flags = FeatureFlagBuilder.createEmptyList()

    // Then
    assertTrue(flags.isEmpty())
  }

  @Test
  fun `should create feature flag with null values`() {
    // When
    val featureFlag = FeatureFlagBuilder.createWithNullValues("test-key")

    // Then
    assertNull(featureFlag.personId)
    assertNull(featureFlag.guarantee)
    assertEquals("test-key", featureFlag.key)
    assertNull(featureFlag.value)
  }

  @Test
  fun `should create feature flag with invalid value`() {
    // When
    val featureFlag = FeatureFlagBuilder.createWithInvalidValue("test-key")

    // Then
    assertEquals("test-key", featureFlag.key)
    assertEquals("invalid-boolean", featureFlag.value)
  }

  @Test
  fun `should create large list`() {
    // When
    val flags = FeatureFlagBuilder.createLargeList(size = 10, baseKey = "test")

    // Then
    assertEquals(10, flags.size)
    flags.forEachIndexed { index, flag ->
      assertEquals("test-${index + 1}", flag.key)
      val expectedValue = if ((index + 1) % 2 == 0) "true" else "false"
      assertEquals(expectedValue, flag.value)
    }
  }

  @Test
  fun `should create large list with custom size`() {
    // When
    val flags = FeatureFlagBuilder.createLargeList(size = 5)

    // Then
    assertEquals(5, flags.size)
  }

  @Test
  fun `should create flags with custom personId and guarantee`() {
    // Given
    val customPersonId = "custom-person"
    val customGuarantee = "custom-guarantee"

    // When
    val flags =
            FeatureFlagBuilder.createMultipleFlags(
                    personId = customPersonId,
                    guarantee = customGuarantee
            )

    // Then
    flags.forEach { flag ->
      assertEquals(customPersonId, flag.personId)
      assertEquals(customGuarantee, flag.guarantee)
    }
  }

  @Test
  fun `should create enabled flags with custom parameters`() {
    // Given
    val keys = listOf("flag1", "flag2")
    val customPersonId = "custom-person"
    val customGuarantee = "custom-guarantee"

    // When
    val flags =
            FeatureFlagBuilder.createEnabledFlags(
                    keys = keys,
                    personId = customPersonId,
                    guarantee = customGuarantee
            )

    // Then
    assertEquals(2, flags.size)
    flags.forEach { flag ->
      assertEquals(customPersonId, flag.personId)
      assertEquals(customGuarantee, flag.guarantee)
      assertEquals("true", flag.value)
    }
  }
}
