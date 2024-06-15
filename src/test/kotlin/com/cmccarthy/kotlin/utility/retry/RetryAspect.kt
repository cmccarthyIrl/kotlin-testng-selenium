package com.cmccarthy.kotlin.utility.retry

import com.cmccarthy.kotlin.utility.annotations.Retryable
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Aspect
class RetryAspect {

    @Pointcut("@annotation(retryable)")
    fun retryableMethods(retryable: Retryable) {
    }

    @Around(value = "retryableMethods(retryable)", argNames = "joinPoint, retryable")
    @Throws(Throwable::class)
    fun retryMethod(joinPoint: ProceedingJoinPoint, retryable: Retryable): Any {
        val maxAttempts = retryable.maxAttempts
        val delay = retryable.backoff.delay

        var attempt = 0
        var lastException: Throwable? = null

        do {
            attempt++
            try {
                return joinPoint.proceed()
            } catch (ex: Throwable) {
                if (isExceptionIncluded(ex, retryable.include)) {
                    lastException = ex
                    if (attempt < maxAttempts) {
                        Thread.sleep(delay)
                    }
                } else {
                    throw ex
                }
            }
        } while (attempt < maxAttempts)

        throw lastException ?: RuntimeException("Retry failed after max attempts")
    }

    private fun isExceptionIncluded(ex: Throwable, includedKClasses: Array<KClass<out Throwable>>): Boolean {
        for (includedKClass in includedKClasses) {
            if (ex::class.isSubclassOf(includedKClass)) {
                return true
            }
        }
        return false
    }
}
