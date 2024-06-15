package com.cmccarthy.kotlin.utility.expectedConditions

import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition

class InvisibilityOfElementByLocator(private val locator: By) : ExpectedCondition<Boolean> {
    override fun apply(d: WebDriver): Boolean {
        return try {
            d.findElement(locator).isDisplayed
        } catch (e: StaleElementReferenceException) {
            true
        } catch (e: NoSuchElementException) {
            true
        } catch (t: Throwable) {
            throw Error(t)
        }
    }
}
