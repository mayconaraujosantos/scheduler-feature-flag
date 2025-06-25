package com.banking.application.exceptions

import com.banking.domain.exceptions.ResourceException

/**
 * Exceção lançada quando há falha no polling de feature flags
 * @param message Mensagem descritiva do erro
 * @param cause Causa original da exceção
 */
class FeatureFlagPollingException(message: String, cause: Throwable? = null) :
        ResourceException(message = message, cause = cause)
