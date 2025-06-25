package com.banking.domain.exceptions

open class ResourceException(
  override val message: String,
  override val cause: Throwable? = null
  ) : C6BaseException(
    type = ResourceException::class.simpleName!!,
    message = message,
    cause = cause
)
