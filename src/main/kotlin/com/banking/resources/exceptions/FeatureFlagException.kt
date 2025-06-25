package com.banking.resources.exceptions

import com.banking.domain.exceptions.ResourceException

class FeatureFlagException(
        override val message: String,
        val statusCode: Int,
        cause: Throwable? = null
) : ResourceException(message = message, cause = cause)
