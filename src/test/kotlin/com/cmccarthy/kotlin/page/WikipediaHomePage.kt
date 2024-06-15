package com.cmccarthy.kotlin.page

import com.cmccarthy.kotlin.utility.annotations.PageObject
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.How

@PageObject
class WikipediaHomePage(driver: WebDriver) : AbstractPage(driver) {
    @FindBy(how = How.XPATH, using = "//a[contains(@href,'commons.wikimedia.org')]")
    val commonPage: WebElement? = null

    @FindBy(how = How.CLASS_NAME, using = "central-featured-logo")
    val CENTRAL_LOGO: WebElement? = null

    fun open(url: String?) {
        openAt(url)
    }
}

