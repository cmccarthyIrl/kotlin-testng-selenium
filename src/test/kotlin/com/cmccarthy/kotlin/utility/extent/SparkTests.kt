package com.cmccarthy.kotlin.utility.extent

import com.aventstack.extentreports.ExtentTest
import com.aventstack.extentreports.MediaEntityBuilder
import com.aventstack.extentreports.Status
import com.cmccarthy.kotlin.utility.DriverManager
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.testng.ITestResult
import java.io.File
import java.nio.file.Files
import java.util.*

object SparkTests {
    // ThreadLocal variables to manage method-level and data provider tests
    private val testThread: ThreadLocal<ExtentTest> =
        ThreadLocal.withInitial { null }
    private val dataProviderThread: ThreadLocal<ExtentTest> = ThreadLocal.withInitial { null }


    @get:Synchronized
    val test: ExtentTest
        // Get the current test being executed
        get() = Optional.ofNullable(dataProviderThread.get()).orElse(testThread.get())

    // Create a test
    @Synchronized
    fun createTest(result: ITestResult) {
        getOrCreateTest(result)
        createTestNode(result)
    }

    // Get or create a test node
    @Synchronized
    fun getOrCreateTest(result: ITestResult): ExtentTest {
        val methodName: String = result.method.methodName
        if (testThread.get() != null && testThread.get().model.name.equals(methodName)) {
            return testThread.get()
        } else {
            val extentTest: ExtentTest =
                SparkReporter.extent.createTest(methodName, result.method.description)
            testThread.set(extentTest)
            assignGroups(extentTest, result.method.groups)
            return extentTest
        }
    }

    // Create a parameter node
    @Synchronized
    private fun createTestNode(result: ITestResult) {
        if (result.parameters.isNotEmpty()) {
            val paramName: String = listOf(*result.parameters).toString()
            val paramTest: ExtentTest = testThread.get().createNode(paramName)
            dataProviderThread.set(paramTest)
        } else {
            dataProviderThread.set(null)
        }
    }

    // Assign groups to the test
    private fun assignGroups(test: ExtentTest, groups: Array<String>) {
        // Assign categories based on group names
        Arrays.stream(groups).map { getCategory(it) }
            .forEach(test::assignCategory)
    }

    // Extract category from group name
    private fun getCategory(group: String): String {
        return if (group.startsWith("d:") || group.startsWith("device:")) group.replace("d:", "")
            .replace("device:", "") else if (group.startsWith("a:") || group.startsWith("author:")) group.replace(
            "a:",
            ""
        ).replace("author:", "") else if (group.startsWith("t:") || group.startsWith("tag:")) group.replace("t:", "")
            .replace("tag:", "") else group
    }

    // Take a screenshot and log it
    @Synchronized
    fun takeScreenShot() {
        val file: File = (DriverManager.getDriver() as TakesScreenshot).getScreenshotAs(OutputType.FILE)
        // Convert screenshot to Base64 string
        val encoded: String = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()))
        // Log the screenshot
        test.addScreenCaptureFromBase64String(encoded).log(
            Status.INFO, "Screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(encoded).build()
        )
    }

}