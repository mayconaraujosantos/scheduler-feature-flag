package com.banking.domain.entities

data class FeatureFlags(
        val personId: String?,
        val guarantee: String?,
        val key: String,
        val value: String?
) {
  companion object {
    fun builder() = FeatureFlagsBuilder()
  }
}

class FeatureFlagsBuilder {
  private var personId: String? = null
  private var guarantee: String? = null
  private var key: String = ""
  private var value: String? = null

  fun personId(personId: String?) = apply { this.personId = personId }
  fun guarantee(guarantee: String?) = apply { this.guarantee = guarantee }
  fun key(key: String) = apply { this.key = key }
  fun value(value: String?) = apply { this.value = value }

  fun build(): FeatureFlags =
          FeatureFlags(personId = personId, guarantee = guarantee, key = key, value = value)
}
