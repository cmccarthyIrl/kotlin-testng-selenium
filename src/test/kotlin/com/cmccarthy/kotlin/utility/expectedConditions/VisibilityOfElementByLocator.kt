package com.cmccarthy.kotlin.utility.expectedConditions

import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition

class VisibilityOfElementByLocator(private val locator: By) : ExpectedCondition<Boolean> {
    override fun apply(d: WebDriver): Boolean {
        return try {
            d.findElement(locator).isDisplayed
        } catch (e: StaleElementReferenceException) {
            false
        } catch (e: NoSuchElementException) {
            false
        } catch (t: Throwable) {
            throw Error(t)
        }
    }
}
