package com.cmccarthy.kotlin.test

import com.cmccarthy.kotlin.page.WikipediaHomePage
import com.cmccarthy.kotlin.utility.DriverManager
import com.cmccarthy.kotlin.utility.DriverWait
import com.cmccarthy.kotlin.utility.config.properties.PropertiesReader
import com.cmccarthy.kotlin.utility.config.properties.PropertyTypes
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testng.annotations.Test

class LoginTest : TestBase() {

    private val testProperties = PropertiesReader.getPropertyFile(PropertyTypes.TEST)
    private val wikipediaHomePage = WikipediaHomePage(DriverManager.getDriver())
    private val log: Logger = LoggerFactory.getLogger(LoginTest::class.java)

    @Test
    fun wikipediaUiTest() {
        val url = testProperties.getProperty("url")
        wikipediaHomePage.open(url)
        log.info("The user navigated to the Wikipedia Homepage : $url")
        click(wikipediaHomePage.commonPage)
        log.info("The user clicked the Common link on the Homepage")
    }
}