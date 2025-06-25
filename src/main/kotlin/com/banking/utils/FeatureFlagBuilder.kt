package com.banking.utils

import com.banking.domain.entities.FeatureFlags

/**
 * Builder utility class for creating FeatureFlags instances with common patterns and default values
 * for testing and development purposes.
 */
object FeatureFlagBuilder {

  // Default values
  private const val DEFAULT_PERSON_ID = "person-123"
  private const val DEFAULT_GUARANTEE = "guarantee-456"
  private const val DEFAULT_KEY = "feature-flag-key"
  private const val DEFAULT_VALUE = "true"
  private const val DEFAULT_INGESTION_KEY = "ENABLE_EVENT_INGESTION_FLAG"

  /** Creates a FeatureFlag with custom values */
  fun create(
          personId: String? = DEFAULT_PERSON_ID,
          guarantee: String? = DEFAULT_GUARANTEE,
          key: String = DEFAULT_KEY,
          value: String? = DEFAULT_VALUE
  ): FeatureFlags =
          FeatureFlags.builder()
                  .personId(personId)
                  .guarantee(guarantee)
                  .key(key)
                  .value(value)
                  .build()

  /** Creates a list of FeatureFlags from vararg parameters */
  fun createList(vararg flags: FeatureFlags): List<FeatureFlags> = flags.toList()

  /** Creates a default enabled feature flag */
  fun createEnabledFlag(
          key: String = DEFAULT_KEY,
          personId: String? = DEFAULT_PERSON_ID,
          guarantee: String? = DEFAULT_GUARANTEE
  ): FeatureFlags = create(personId = personId, guarantee = guarantee, key = key, value = "true")

  /** Creates a default disabled feature flag */
  fun createDisabledFlag(
          key: String = DEFAULT_KEY,
          personId: String? = DEFAULT_PERSON_ID,
          guarantee: String? = DEFAULT_GUARANTEE
  ): FeatureFlags = create(personId = personId, guarantee = guarantee, key = key, value = "false")

  /** Creates an ingestion enabled flag with the specified key */
  fun createIngestionEnabledFlag(
          ingestionKey: String = DEFAULT_INGESTION_KEY,
          personId: String? = DEFAULT_PERSON_ID,
          guarantee: String? = DEFAULT_GUARANTEE
  ): FeatureFlags =
          create(personId = personId, guarantee = guarantee, key = ingestionKey, value = "true")

  /** Creates an ingestion disabled flag with the specified key */
  fun createIngestionDisabledFlag(
          ingestionKey: String = DEFAULT_INGESTION_KEY,
          personId: String? = DEFAULT_PERSON_ID,
          guarantee: String? = DEFAULT_GUARANTEE
  ): FeatureFlags =
          create(personId = personId, guarantee = guarantee, key = ingestionKey, value = "false")

  /** Creates multiple feature flags for common scenarios */
  fun createMultipleFlags(
          ingestionKey: String = DEFAULT_INGESTION_KEY,
          personId: String? = DEFAULT_PERSON_ID,
          guarantee: String? = DEFAULT_GUARANTEE
  ): List<FeatureFlags> =
          listOf(
                  createIngestionEnabledFlag(ingestionKey, personId, guarantee),
                  create(
                          key = "other-feature-1",
                          value = "true",
                          personId = personId,
                          guarantee = guarantee
                  ),
                  create(
                          key = "other-feature-2",
                          value = "false",
                          personId = personId,
                          guarantee = guarantee
                  ),
                  create(
                          key = "optional-feature",
                          value = null,
                          personId = personId,
                          guarantee = guarantee
                  )
          )

  /** Creates a list of enabled flags */
  fun createEnabledFlags(
          keys: List<String>,
          personId: String? = DEFAULT_PERSON_ID,
          guarantee: String? = DEFAULT_GUARANTEE
  ): List<FeatureFlags> = keys.map { key -> createEnabledFlag(key, personId, guarantee) }

  /** Creates a list of disabled flags */
  fun createDisabledFlags(
          keys: List<String>,
          personId: String? = DEFAULT_PERSON_ID,
          guarantee: String? = DEFAULT_GUARANTEE
  ): List<FeatureFlags> = keys.map { key -> createDisabledFlag(key, personId, guarantee) }

  /** Creates an empty list of feature flags */
  fun createEmptyList(): List<FeatureFlags> = emptyList()

  /** Creates a feature flag with null values (for testing edge cases) */
  fun createWithNullValues(key: String = DEFAULT_KEY): FeatureFlags =
          create(personId = null, guarantee = null, key = key, value = null)

  /** Creates a feature flag with invalid boolean value (for testing edge cases) */
  fun createWithInvalidValue(
          key: String = DEFAULT_KEY,
          personId: String? = DEFAULT_PERSON_ID,
          guarantee: String? = DEFAULT_GUARANTEE
  ): FeatureFlags =
          create(personId = personId, guarantee = guarantee, key = key, value = "invalid-boolean")

  /** Creates a large list of feature flags for performance testing */
  fun createLargeList(
          size: Int = 100,
          baseKey: String = "feature-flag",
          personId: String? = DEFAULT_PERSON_ID,
          guarantee: String? = DEFAULT_GUARANTEE
  ): List<FeatureFlags> =
          (1..size).map { index ->
            create(
                    personId = personId,
                    guarantee = guarantee,
                    key = "$baseKey-$index",
                    value = if (index % 2 == 0) "true" else "false"
            )
          }
}
