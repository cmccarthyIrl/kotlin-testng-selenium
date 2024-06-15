package com.cmccarthy.kotlin.utility.expectedConditions

import com.cmccarthy.kotlin.utility.constants.ConstantTest
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Wait
import java.time.Duration

class
ClickabilityOfElement(element: WebElement) : ExpectedCondition<WebElement> {
    private val element: WebElement = element

    override fun apply(webDriver: WebDriver): WebElement {
        val wait: Wait<WebDriver> = FluentWait(webDriver)
            .withTimeout(Duration.ofSeconds(ConstantTest.WAIT_SHORT))
            .pollingEvery(Duration.ofMillis(ConstantTest.POLLING_SHORT))
            .ignoring(
                java.util.NoSuchElementException::class.java,
                StaleElementReferenceException::class.java
            )
        return try {
            wait.until(ExpectedConditions.elementToBeClickable(element))
        } catch (exception: StaleElementReferenceException) {
            element
        } catch (exception: NoSuchElementException) {
            element
        } catch (t: Throwable) {
            throw Error(t)
        }
    }
}
