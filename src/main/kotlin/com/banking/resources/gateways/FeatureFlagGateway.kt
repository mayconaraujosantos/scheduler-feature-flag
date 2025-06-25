package com.banking.resources.gateways

import com.banking.application.config.FeatureFlagConfig
import com.banking.domain.entities.FeatureFlags
import com.banking.resources.exceptions.FeatureFlagException
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.isSuccessful
import io.micrometer.core.annotation.Timed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
open class FeatureFlagGateway(
        @Qualifier("featureFlagObjectMapper") private val objectMapper: ObjectMapper,
        private val featureFlagConfig: FeatureFlagConfig
) {

  private val logger: Logger = LoggerFactory.getLogger(FeatureFlagGateway::class.java)
  private val logPrefix = "[${FeatureFlagGateway::class.simpleName}]"

  @Timed(
          value = "feature.flag.api.request",
          description = "Time spent making feature flag API requests"
  )
  open fun getFeatureFlagFromApi(): List<FeatureFlags> {
    val fullUrl = "${featureFlagConfig.apiUrl}?keys=${featureFlagConfig.ingestionKey}"
    logger.info("$logPrefix: About to get feature flags from API on URL $fullUrl")

    val (_, response, _) =
            Fuel.get(path = fullUrl)
                    .header(
                            map =
                                    mapOf(
                                            "Accept" to "application/json",
                                            "Authorization" to featureFlagConfig.authToken
                                    )
                    )
                    .timeoutRead(timeout = featureFlagConfig.apiTimout)
                    .timeout(timeout = featureFlagConfig.apiTimout)
                    .response()

    if (!response.isSuccessful) {
      throw handleHttpError(fullUrl, response.statusCode, response.data)
    }

    val featureFlags =
            objectMapper.readValue(response.data, Array<FeatureFlags>::class.java).toList().also {
              logger.info("$logPrefix: Successfully parsed response data")
            }

    // Log do conteúdo da resposta
    logResponseContent(featureFlags)

    logger.info("$logPrefix: Successfully retrieved ${featureFlags.size} feature flags from API")
    return featureFlags
  }

  private fun logResponseContent(featureFlags: List<FeatureFlags>) {
    logger.debug("$logPrefix: Response Content (${featureFlags.size} feature flags):")

    if (featureFlags.isEmpty()) {
      logger.warn("$logPrefix: No feature flags returned from API")
      return
    }

    featureFlags.forEachIndexed { index, flag ->
      logger.debug("$logPrefix:   [$index] ${flag.key} = ${flag.value}")
    }

    // Log resumido para produção
    val enabledFlags = featureFlags.filter { flag -> flag.value?.toBooleanStrictOrNull() == true }
    val disabledFlags = featureFlags.filter { flag -> flag.value?.toBooleanStrictOrNull() == false }

    logger.info("$logPrefix: Feature Flags Summary:")
    logger.info("$logPrefix: - Total: ${featureFlags.size}")
    logger.info("$logPrefix: - Enabled: ${enabledFlags.size}")
    logger.info("$logPrefix: - Disabled: ${disabledFlags.size}")

    if (enabledFlags.isNotEmpty()) {
      logger.info("$logPrefix: - Enabled flags: ${enabledFlags.map { it.key }}")
    }
  }

  private fun handleHttpError(url: String, statusCode: Int, data: ByteArray): Throwable {
    val rawData = String(bytes = data)
    val message = "Request to $url failed with status code $statusCode. Raw response: $rawData"
    logger.error("$logPrefix: $message")
    return FeatureFlagException(message, statusCode)
  }

  private fun handleParsingError(
          url: String,
          statusCode: Int,
          data: ByteArray,
          cause: Throwable
  ): Throwable {
    val rawData = String(bytes = data)
    val message = "Failed to parse API response from $url. Raw response: $rawData"
    logger.error("$logPrefix: $message", cause)
    return FeatureFlagException(message, statusCode, cause)
  }
}
