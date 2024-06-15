package com.cmccarthy.kotlin.utility.expectedConditions

import com.cmccarthy.kotlin.utility.constants.ConstantTest
import org.openqa.selenium.*
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Wait
import java.time.Duration

class ClickabilityOfElementByLocator(locator: By) : ExpectedCondition<WebElement> {
    private val locator: By = locator

    override fun apply(webDriver: WebDriver): WebElement {
        val wait: Wait<WebDriver> = FluentWait(webDriver)
            .withTimeout(Duration.ofSeconds(ConstantTest.WAIT_SHORT))
            .pollingEvery(Duration.ofMillis(ConstantTest.POLLING_SHORT))
            .ignoring(
                java.util.NoSuchElementException::class.java,
                StaleElementReferenceException::class.java
            )

        return try {
            wait.until(ExpectedConditions.elementToBeClickable(locator))
        } catch (e: StaleElementReferenceException) {
            webDriver.findElement(locator)
        } catch (e: NoSuchElementException) {
            webDriver.findElement(locator)
        } catch (t: Throwable) {
            throw Error(t)
        }
    }
}
