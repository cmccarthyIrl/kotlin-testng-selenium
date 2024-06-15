package com.cmccarthy.kotlin.utility.listeners

import com.aventstack.extentreports.Status
import com.cmccarthy.kotlin.utility.extent.SparkReporter
import com.cmccarthy.kotlin.utility.extent.SparkTests
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testng.*

/**
 * TestListener class for handling TestNG test events.
 */
class TestListener : ITestListener, ISuiteListener {

    private val logger: Logger = LoggerFactory.getLogger(TestListener::class.java)

    override fun onTestStart(result: ITestResult) {
        logger.info("============= Starting test: " + result.name + " =============")
    }

    override fun onTestSuccess(result: ITestResult) {
        updateExtentTest(result, Status.PASS)
    }

    override fun onTestFailure(result: ITestResult) {
        updateExtentTest(result, Status.FAIL)
    }

    override fun onTestSkipped(result: ITestResult) {
        updateExtentTest(result, Status.SKIP)
    }

    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult) {
    }

    override fun onFinish(suite: ISuite) {
        logger.debug("Creating the Spark HTML report")
        SparkReporter.extent.flush()
    }

    /**
     * Updates the extent test based on the test result.
     *
     * @param iTestResult The TestNG result.
     * @param status      The status of the test.
     */
    private fun updateExtentTest(iTestResult: ITestResult, status: Status) {
        if (iTestResult.throwable != null) {
            if (status == Status.SKIP &&
                (iTestResult.throwable.javaClass == SkipException::class.java || iTestResult.throwable.javaClass == AssertionError::class.java)
                && iTestResult.testContext.failedTests.getResults(iTestResult.method).isEmpty()
                && iTestResult.testContext.skippedTests.getResults(iTestResult.method).isEmpty()
            ) {
                logger.warn(iTestResult.toString())
                logger.warn("============= Skipped test: " + iTestResult.name + " ==============")
                SparkTests.test.log(Status.INFO, "============= Skipped test: " + iTestResult.name + " ==============")
            } else {
                logger.error(iTestResult.toString())
                logger.error("============= Failed test: " + iTestResult.name + " ==============")
                SparkTests.test.fail("============= Failed test: " + iTestResult.name + " ==============")
            }
        } else {
            logger.info("============= Passed test: " + iTestResult.name + " ==============")
            SparkTests.test.pass("============= Passed test: " + iTestResult.name + " ==============")
        }
    }
}