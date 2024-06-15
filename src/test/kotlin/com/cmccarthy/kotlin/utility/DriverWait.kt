package com.cmccarthy.kotlin.utility

import com.cmccarthy.kotlin.utility.constants.ConstantTest
import com.cmccarthy.kotlin.utility.expectedConditions.*
import org.openqa.selenium.*
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Wait
import java.time.Duration

class DriverWait {

    private val driver = DriverManager.getDriver()

    init {
        FluentWait(driver).apply {
            withTimeout(Duration.ofSeconds(ConstantTest.WAIT_SHORT))
            pollingEvery(Duration.ofMillis(ConstantTest.POLLING_SHORT))
            ignoring(NoSuchElementException::class.java, StaleElementReferenceException::class.java)
        }
    }

    companion object {
        @Throws(NoSuchFieldException::class)
        fun waitForElementToLoad(element: WebElement) {
            waitForAngular()
            waitForElementVisible(element)
            waitForElementClickable(element)
        }

        @Throws(NoSuchFieldException::class)
        fun waitForElementToLoad(locator: By) {
            waitForAngular()
            waitForElementVisible(locator)
            waitForElementClickable(locator)
        }

        private fun waitForElementVisible(element: WebElement) {
            waitLong().until(VisibilityOfElement(element))
        }

        private fun waitForElementVisible(locator: By) {
            waitLong().until(VisibilityOfElementByLocator(locator))
        }

        private fun waitForElementInvisible(locator: By) {
            waitLong().until(InvisibilityOfElementByLocator(locator))
        }

        private fun waitForElementInvisible(element: WebElement) {
            waitLong().until(InvisibilityOfElement(element))
        }

        @Throws(NoSuchFieldException::class)
        private fun waitForElementClickable(element: WebElement) {
            waitLong().until(ClickabilityOfElement(element))
        }

        @Throws(NoSuchFieldException::class)
        private fun waitForElementClickable(locator: By) {
            waitLong().until(ClickabilityOfElementByLocator(locator))
        }

        fun waitLong(): Wait<WebDriver> {
            return FluentWait(DriverManager.getDriver()).apply {
                withTimeout(Duration.ofSeconds(ConstantTest.WAIT_LONG))
                pollingEvery(Duration.ofMillis(ConstantTest.POLLING_LONG))
                ignoring(NoSuchElementException::class.java, StaleElementReferenceException::class.java)
            }
        }

        fun waitShort(): Wait<WebDriver> {
            return FluentWait(DriverManager.getDriver()).apply {
                withTimeout(Duration.ofSeconds(ConstantTest.WAIT_SHORT))
                pollingEvery(Duration.ofMillis(ConstantTest.POLLING_SHORT))
                ignoring(NoSuchElementException::class.java, StaleElementReferenceException::class.java)
            }
        }

        fun waitForAngular() {
            if (!isAngularUnDefined()) {
                waitForAngularLoad()
                waitUntilJSReady()
                waitForJQueryLoad()
            }
        }

        private fun isAngularUnDefined(): Boolean {
            return (DriverManager.getDriver() as JavascriptExecutor).executeScript("return window.angular === undefined") as Boolean
        }

        private fun waitForAngularLoad() {
            val angularReadyScript =
                "return angular.element(document).injector().get('\$http').pendingRequests.length === 0"
            waitLong().until(ExpectedCondition<Boolean> {
                (DriverManager.getDriver() as JavascriptExecutor).executeScript(angularReadyScript).toString()
                    .toBoolean()
            })
        }

        private fun waitUntilJSReady() {
            waitLong().until(ExpectedCondition<Boolean> {
                (DriverManager.getDriver() as JavascriptExecutor).executeScript("return document.readyState")
                    .toString() == "complete"
            })
        }

        private fun waitForJQueryLoad() {
            waitLong().until(ExpectedCondition<Boolean> {
                (DriverManager.getDriver() as JavascriptExecutor).executeScript("return jQuery.active") as Long == 0L
            })
        }
    }
}
