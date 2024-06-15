package com.cmccarthy.kotlin.page

import com.cmccarthy.kotlin.utility.DriverManager
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

abstract class AbstractPage(private val driver: WebDriver) {

    private val pageLoadTimeout: Long = 30

    init {
        PageFactory.initElements(driver, this)
    }

    protected fun openAt(url: String?) {
        DriverManager.getDriver().get(url)
        waitForPageLoad(driver)
    }

    open fun waitForPageLoad(driver: WebDriver) {
        val pageLoadCondition: ExpectedCondition<Boolean> =
            ExpectedCondition { (driver as JavascriptExecutor).executeScript("return document.readyState") == "complete" }
        val wait = WebDriverWait(driver, Duration.ofSeconds(pageLoadTimeout))
        wait.until(pageLoadCondition)
    }
}