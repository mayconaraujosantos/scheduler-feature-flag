package com.banking.application.scheduler

import com.banking.application.exceptions.FeatureFlagPollingException
import com.banking.domain.dataccess.FeatureFlagDA
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class FeatureFlagScheduler(private val featureFlagDA: FeatureFlagDA) {
  private val logger = org.slf4j.LoggerFactory.getLogger(FeatureFlagScheduler::class.java)
  private val logPrefix = "[${FeatureFlagScheduler::class.simpleName}]"

  @Scheduled(
          fixedRateString = "\${feature.flag.poll.interval-ms}",
          initialDelayString = "\${feature.flag.initial-delay-ms:5000}"
  )
  @Synchronized
  fun pollFeatureFlags() {
    logger.info("$logPrefix: Starting scheduled poll for feature flags...")
    try {
      val featureFlags = featureFlagDA.getFeatureFlag()
      logger.info("$logPrefix: Successfully polled ${featureFlags.size} feature flags")

      // Log the status of specific feature flags if needed
      featureFlags.forEach { flag ->
        logger.debug("$logPrefix: Feature flag ${flag.key} = ${flag.value}")
      }
    } catch (e: Exception) {
      logger.error("$logPrefix: Error during scheduled feature flag poll: ${e.message}", e)
      // Consider implementing retry logic or circuit breaker here
      throw FeatureFlagPollingException("Failed to poll feature flags", e)
    }
    logger.info("$logPrefix: Finished scheduled poll for feature flags.")
  }
}
