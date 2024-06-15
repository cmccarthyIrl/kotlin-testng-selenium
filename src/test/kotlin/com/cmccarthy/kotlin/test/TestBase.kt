package com.cmccarthy.kotlin.test

import com.cmccarthy.kotlin.utility.DriverHelper
import com.cmccarthy.kotlin.utility.DriverManager
import com.cmccarthy.kotlin.utility.extent.SparkTests
import com.cmccarthy.kotlin.utility.listeners.TestListener
import org.openqa.selenium.WebDriver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testng.ITestResult
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.BeforeSuite
import org.testng.annotations.Listeners
import java.lang.reflect.Method

@Listeners(TestListener::class)
abstract class TestBase : DriverHelper() {
    private val log: Logger = LoggerFactory.getLogger(TestBase::class.java)
    private lateinit var driver: WebDriver

    @BeforeSuite
    fun beforeSuite() {
        try {
            driver = DriverManager.getDriver()
            DriverManager.configureDriver(driver)
        } catch (e: Exception) {
            log.info("Failed to initialize WebDriver: ${e.message}")
        }
    }

    @BeforeMethod(alwaysRun = true)
    fun beforeMethod(iTestResult: ITestResult, method: Method) {
        logger.debug("Started: beforeMethod")
        SparkTests.createTest(iTestResult)
        logger.debug("Finished: beforeMethod")
    }

    @AfterMethod
    fun afterMethod() {
        try {
            DriverManager.clearDriver()
        } catch (e: Exception) {
            log.info("Failed to close WebDriver: ${e.message}")
        }
    }
}
