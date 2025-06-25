package com.banking.domain.exceptions


open class C6BaseException(
  open val type: String,
  override val message: String = "Internal Server Error",
  open val details: Map<String, Any> = mutableMapOf(),
  override val cause: Throwable? = null
) : RuntimeException(message, cause)

