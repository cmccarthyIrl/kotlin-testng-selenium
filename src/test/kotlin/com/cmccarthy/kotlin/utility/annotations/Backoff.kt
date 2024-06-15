package com.cmccarthy.kotlin.utility.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Backoff(
    val value: Long = 1000L,
    val delay: Long = 1000L,
    val maxDelay: Long = 0L,
    val multiplier: Double = 0.0,
    val delayExpression: String = "",
    val maxDelayExpression: String = "",
    val multiplierExpression: String = "",
    val random: Boolean = false,
    val randomExpression: String = ""
)
