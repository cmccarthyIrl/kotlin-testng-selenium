package com.cmccarthy.kotlin.page

import com.cmccarthy.kotlin.utility.annotations.PageObject
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.How

@PageObject
class WikipediaCommonPage(driver: WebDriver) : AbstractPage(driver) {
    @FindBy(how = How.CLASS_NAME, using = "mainpage-welcome-sitename")
    val centralLogo: WebElement? = null
}

