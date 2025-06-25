package com.banking.domain.services

import com.banking.application.config.FeatureFlagConfig
import com.banking.domain.dataccess.FeatureFlagDA
import org.springframework.stereotype.Service

@Service
class IntegrationBufferService(
        private val featureFlagDA: FeatureFlagDA,
        private val featureFlagConfig: FeatureFlagConfig
) {
  private val logger = org.slf4j.LoggerFactory.getLogger(IntegrationBufferService::class.java)

  /**
   * Método para bufferizar um evento. A ação de bufferizar é condicionada ao estado do feature flag
   * 'ENABLE_EVENT_INGESTION_FLAG'.
   *
   * @param event O evento (representado aqui como uma String simples para demonstração).
   */
  fun bufferEvent(event: String) {
    logger.info("Attempting to buffer event: '$event'")

    // Verifica o estado do feature flag de ingestão de eventos.
    if (isEventIngestionEnabled()) {
      logger.info(
              "Feature flag 'ENABLE_EVENT_INGESTION_FLAG' is ENABLED. Proceeding with event buffering..."
      )
      // --- Lógica REAL de bufferização do evento vai aqui ---
      // Por exemplo:
      // - Enviar para uma fila (Kafka, SQS)
      // - Salvar em um banco de dados temporário
      // - Chamar outro serviço de integração
      // ----------------------------------------------------
      logger.info("Event '$event' successfully buffered.")
    } else {
      // Se o feature flag estiver desabilitado, o evento não é bufferizado.
      logger.warn(
              "Feature flag 'ENABLE_EVENT_INGESTION_FLAG' is DISABLED. Event '$event' will NOT be buffered."
      )
      // --- Lógica para lidar com a ingestão desabilitada ---
      // Por exemplo:
      // - Descartar o evento
      // - Registrar um log de que o evento foi ignorado
      // - Enviar uma notificação (se for um erro inesperado)
      // ----------------------------------------------------
    }
  }

  /**
   * Verifica se a ingestão de eventos está habilitada
   * @return true se a ingestão estiver habilitada
   */
  private fun isEventIngestionEnabled(): Boolean {
    return try {
      val featureFlags = featureFlagDA.getFeatureFlag()
      val enableIngestionFlag =
              featureFlags.firstOrNull { it.key == featureFlagConfig.ingestionKey }
      enableIngestionFlag?.value?.toBooleanStrictOrNull() ?: false
    } catch (e: Exception) {
      logger.error("Error checking event ingestion flag: ${e.message}")
      false
    }
  }

  // Você pode ter outros métodos neste serviço que também dependam de feature flags
  // ou que realizem outras operações relacionadas à integração.
}
