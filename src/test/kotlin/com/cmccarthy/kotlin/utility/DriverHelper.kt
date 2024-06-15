package com.cmccarthy.kotlin.utility

import com.cmccarthy.kotlin.utility.annotations.Backoff
import com.cmccarthy.kotlin.utility.annotations.Retryable
import com.cmccarthy.kotlin.utility.retry.RetryException
import org.openqa.selenium.By
import org.openqa.selenium.ElementNotInteractableException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
open class DriverHelper {

    private val logger: Logger = LoggerFactory.getLogger(DriverHelper::class.java)

    /**
     * Send Keys to the specified element, clears the element first
     */
    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 500), include = [RetryException::class])
    fun sendKeys(element: WebElement, value: String?) {
        if (value != null) {
            if (value.length > 0) {
                clear(element)
                element.sendKeys(value)
            } else {
                clear(element)
            }
        }
    }

    /**
     * Clicks on an element by WebElement
     */
    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 500), include = [RetryException::class])
    fun click(element: WebElement?) {
        try {
            element?.let {
                DriverWait.waitForElementToLoad(it)
                element.click()
            }
        } catch (e: StaleElementReferenceException) {
            logger.warn("Could not click on the element")
            throw RetryException("Could not click on the element : $element")
        }
    }

    /**
     * Clicks on an element by Locator
     */
    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 500), include = [RetryException::class])
    fun click(locator: By) {
        try {
            DriverWait.waitForElementToLoad(locator)
            DriverManager.getDriver().findElement(locator).click()
        } catch (e: StaleElementReferenceException) {
            logger.warn("Could not click on the element")
            throw RetryException("Could not click on the element")
        }
    }

    /**
     * Clicks on an element by Locator
     */
    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 500), include = [RetryException::class])
    fun rightClick(locator: By) {
        DriverWait.waitForElementToLoad(locator)
        val element: WebElement = DriverManager.getDriver().findElement(locator)
        try {
            val builder = Actions(DriverManager.getDriver())
            builder.moveToElement(element).contextClick(element)
            builder.perform()
        } catch (ser: Exception) {
            logger.warn("Could not click on the element : $element")
            throw RetryException("Could not click on the element : $element")
        }
    }

    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 500), include = [RetryException::class])
    fun scrollElementIntoView(element: WebElement) {
        try {
            DriverManager.getJSExecutor().executeScript("arguments[0].scrollIntoView(true);", element)
        } catch (ignored: Exception) {
            logger.warn("Could not click on the element : $element")
            throw RetryException("Could not click on the element : $element")
        }
    }

    /**
     * Clicks on an element by WebElement
     */
    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 500), include = [RetryException::class])
    fun rightClick(element: WebElement) {
        DriverWait.waitForElementToLoad(element)

        try {
            val builder = Actions(DriverManager.getDriver())
            builder.moveToElement(element).contextClick(element)
            builder.perform()
        } catch (ser: Exception) {
            logger.warn("Could not click on the element : $element")
            throw RetryException("Could not click on the element : $element")
        }
    }

    /**
     * Clicks on an element using Actions
     */
    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 500), include = [RetryException::class])
    fun clickAction(element: WebElement) {
        DriverWait.waitForElementToLoad(element)
        try {
            val builder = Actions(DriverManager.getDriver())
            builder.moveToElement(element).click(element)
            builder.perform()
        } catch (ser: Exception) {
            logger.warn("Could not click on the element")
            throw RetryException("Could not click on the element : $element")
        }
    }

    /**
     * Clicks on an element using Actions
     */
    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 500), include = [RetryException::class])
    fun clickAction(locator: By) {
        DriverWait.waitForElementToLoad(locator)

        val element: WebElement = DriverManager.getDriver().findElement(locator)
        try {
            val builder = Actions(DriverManager.getDriver())
            builder.moveToElement(element).click(element)
            builder.perform()
        } catch (ser: Exception) {
            logger.warn("Could not click on the element")
            throw RetryException("Could not click on the element : $element")
        }
    }

    /**
     * Checks if the specified element is displayed
     */
    fun isElementDisplayed(element: WebElement): Boolean {
        var present = false
        try {
            present = element.isDisplayed
        } catch (ignored: Exception) {
        }
        return present
    }

    /**
     * Clear text from a field
     */
    private fun clear(element: WebElement) {
        try {
            DriverManager.getJSExecutor().executeScript("arguments[0].value='';", element)
        } catch (ignored: ElementNotInteractableException) {
        }
    }
}
