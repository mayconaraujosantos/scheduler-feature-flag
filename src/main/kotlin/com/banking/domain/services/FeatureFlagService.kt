package com.banking.domain.services

import com.banking.application.config.FeatureFlagConfig
import com.banking.domain.dataccess.FeatureFlagDA
import org.springframework.stereotype.Service

@Service
class FeatureFlagService(
        private val featureFlagConfig: FeatureFlagConfig,
        private val featureFlagDA: FeatureFlagDA
) {
  private val logger = org.slf4j.LoggerFactory.getLogger(FeatureFlagService::class.java)
  private val logPrefix = "[${FeatureFlagService::class.simpleName}]"

  /**
   * Obtém o valor do recurso de ingestão de eventos.
   * @return true se o recurso estiver habilitado, false caso contrário.
   */
  fun getEventIngestionFlag(): Boolean {
    logger.info("$logPrefix: Fetching feature flags ${featureFlagConfig.ingestionKey}")

    return try {
      val featureFlags = featureFlagDA.getFeatureFlag()
      val enableIngestionFlag =
              featureFlags.firstOrNull { it.key == featureFlagConfig.ingestionKey }
      enableIngestionFlag?.value?.toBooleanStrictOrNull() ?: false
    } catch (e: Exception) {
      logger.error("$logPrefix: Error fetching feature flags: ${e.message}")
      false
    }
  }
}
