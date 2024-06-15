package com.cmccarthy.kotlin.utility.expectedConditions

import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition

class InvisibilityOfElement(private val element: WebElement) : ExpectedCondition<Boolean> {
    override fun apply(d: WebDriver): Boolean {
        return try {
            element.isDisplayed
        } catch (e: StaleElementReferenceException) {
            true
        } catch (e: NoSuchElementException) {
            true
        } catch (t: Throwable) {
            throw Error(t)
        }
    }
}
