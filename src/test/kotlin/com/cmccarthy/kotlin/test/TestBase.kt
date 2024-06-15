package com.cmccarthy.kotlin.test

import com.cmccarthy.kotlin.utility.DriverHelper
import com.cmccarthy.kotlin.utility.DriverManager
import com.cmccarthy.kotlin.utility.DriverWait
import org.openqa.selenium.WebDriver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod

abstract class TestBase : DriverHelper() {
    private val log: Logger = LoggerFactory.getLogger(TestBase::class.java)
    private lateinit var driver: WebDriver

    @BeforeMethod
    open fun setup() {
        try {
            driver = DriverManager.getDriver()
            DriverManager.configureDriver(driver)
        } catch (e: Exception) {
            log.info("Failed to initialize WebDriver: ${e.message}")
        }
    }

    @AfterMethod
    open fun tearDown() {
        try {
            DriverManager.clearDriver()
        } catch (e: Exception) {
            log.info("Failed to close WebDriver: ${e.message}")
            // Log or handle exception if clearing WebDriver fails
        }
    }
}
