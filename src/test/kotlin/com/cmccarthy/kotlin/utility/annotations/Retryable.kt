package com.cmccarthy.kotlin.utility.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Retryable(
    val recover: String = "",
    val interceptor: String = "",
    val include: Array<KClass<out Throwable>> = [],
    val notRecoverable: Array<KClass<out Throwable>> = [],
    val label: String = "",
    val stateful: Boolean = false,
    val maxAttempts: Int = 3,
    val maxAttemptsExpression: String = "",
    val backoff: Backoff = Backoff(),
    val exceptionExpression: String = "",
    val listeners: Array<String> = []
)