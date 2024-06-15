package com.cmccarthy.kotlin.test

import com.cmccarthy.kotlin.page.WikipediaCommonPage
import com.cmccarthy.kotlin.page.WikipediaHomePage
import com.cmccarthy.kotlin.utility.DriverManager
import com.cmccarthy.kotlin.utility.config.properties.PropertiesReader
import com.cmccarthy.kotlin.utility.config.properties.PropertyTypes
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testng.annotations.Test
import kotlin.test.assertTrue

class WikipediaTest : TestBase() {

    private val testProperties = PropertiesReader.getPropertyFile(PropertyTypes.TEST)
    private val wikipediaHomePage = WikipediaHomePage(DriverManager.getDriver())
    private val wikipediaCommonPage = WikipediaCommonPage(DriverManager.getDriver())
    private val log: Logger = LoggerFactory.getLogger(WikipediaTest::class.java)

    @Test
    fun wikipediaUiTest() {
        val url = testProperties.getProperty("url")
        wikipediaHomePage.open(url)
        log.info("The user navigated to the Wikipedia Homepage : $url")
        click(wikipediaHomePage.commonPage)
        log.info("The user clicked the Common link on the Homepage")
        val pageTitle = wikipediaCommonPage.centralLogo!!.isDisplayed
        assertTrue(pageTitle, "Expected the common page to be displayed")
    }
}