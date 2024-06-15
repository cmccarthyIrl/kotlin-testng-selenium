package com.cmccarthy.kotlin.utility

import com.cmccarthy.kotlin.utility.config.properties.PropertiesReader
import com.cmccarthy.kotlin.utility.config.properties.PropertyTypes
import com.cmccarthy.kotlin.utility.constants.ConstantFramework
import com.codeborne.selenide.WebDriverRunner
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.ie.InternetExplorerOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.safari.SafariOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.time.Duration

class DriverManager {
    private val log: Logger = LoggerFactory.getLogger(DriverManager::class.java)
    private val frameworkProperties = PropertiesReader.getPropertyFile(PropertyTypes.CONFIG)
    private val testProperties = PropertiesReader.getPropertyFile(PropertyTypes.TEST)

    init {
        if (!isDriverExisting()) {
            downloadDriver()
        }
    }

    companion object {
        private val driverThreadLocal = ThreadLocal<WebDriver>()

        fun getDriver(): WebDriver {
            if (driverThreadLocal.get() == null) {
                createDriver()
            }
            return driverThreadLocal.get()
                ?: throw IllegalStateException("Driver is not initialized. Call createDriver() first.")
        }

        private fun createDriver() {
            DriverManager().initializeDriver()
        }

        fun clearDriver() {
            driverThreadLocal.get()?.quit()
            driverThreadLocal.remove()
        }

        fun configureDriver(driver: WebDriver) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30))
            driver.manage().window().maximize()
        }

        fun getJSExecutor(): JavascriptExecutor {
            return getDriver() as JavascriptExecutor
        }
    }

    @Throws(IOException::class)
    fun initializeDriver() {
        if (testProperties.getProperty("environment").toString().contains("cloud-provider")) {
            setRemoteDriver(URL(frameworkProperties.getProperty("gridUrl")))
        } else {
            setLocalWebDriver()
        }
        WebDriverRunner.setWebDriver(getDriver())
        WebDriverRunner.getWebDriver().manage().deleteAllCookies()
    }

    @Throws(IOException::class)
    private fun setLocalWebDriver() {
        when (testProperties.getProperty("browser")) {
            "chrome" -> {
                val path = ConstantFramework.DRIVER_DIRECTORY

                val src = ChromeDriverService.Builder()
                    .usingDriverExecutable(File(path + "/chromedriver" + getExtension()))
                    .usingAnyFreePort().build()
                src.start()

                System.setProperty("webdriver.chrome.driver", path + "/chromedriver" + getExtension())

                val options = ChromeOptions()
                options.addArguments("--disable-logging")
                options.addArguments("--no-sandbox")
                options.addArguments("--disable-dev-shm-usage")
                options.addArguments("--headless=new")
                driverThreadLocal.set(ChromeDriver(src, options))
            }

            "firefox" -> {
                System.setProperty(
                    "webdriver.gecko.driver",
                    ConstantFramework.DRIVER_DIRECTORY + "/geckodriver" + getExtension()
                )

                val options = FirefoxOptions()
                options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe")
                val driver: WebDriver = FirefoxDriver(options)
                driverThreadLocal.set(driver)
            }

            "ie" -> {
                System.setProperty(
                    "webdriver.ie.driver",
                    ConstantFramework.DRIVER_DIRECTORY + "/IEDriverServer" + getExtension()
                )
                val capabilitiesIE = InternetExplorerOptions()
                capabilitiesIE.setCapability(
                    InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                    true
                )
                driverThreadLocal.set(InternetExplorerDriver(capabilitiesIE))
            }

            "safari" -> {
                val safariOptions = SafariOptions()
                System.setProperty(
                    "webdriver.safari.driver",
                    ConstantFramework.DRIVER_DIRECTORY + "/safaridriver" + getExtension()
                )
                driverThreadLocal.set(SafariDriver(safariOptions))
            }

            "edge" -> {
                val edgeOptions = EdgeOptions()
                System.setProperty(
                    "webdriver.edge.driver",
                    ConstantFramework.DRIVER_DIRECTORY + "/MicrosoftWebDriver" + getExtension()
                )
                driverThreadLocal.set(EdgeDriver(edgeOptions))
            }

            else -> throw NoSuchElementException(
                "Failed to create an instance of WebDriver for: " + testProperties.getProperty(
                    "browser"
                )
            )
        }
    }

    @Throws(IOException::class)
    private fun setRemoteDriver(hubUrl: URL) {
        val capability: Capabilities
        when (testProperties.getProperty("browser")) {
            "firefox" -> {
                capability = FirefoxOptions()
                driverThreadLocal.set(RemoteWebDriver(hubUrl, capability))
            }

            "chrome" -> {
                val options = ChromeOptions()
                options.addArguments("--no-sandbox")
                options.addArguments("--disable-dev-shm-usage")
                options.addArguments("--headless")
                driverThreadLocal.set(RemoteWebDriver(hubUrl, options))
            }

            "ie" -> {
                capability = InternetExplorerOptions()
                driverThreadLocal.set(RemoteWebDriver(hubUrl, capability))
            }

            "safari" -> {
                capability = SafariOptions()
                driverThreadLocal.set(RemoteWebDriver(hubUrl, capability))
            }

            "edge" -> {
                capability = EdgeOptions()
                driverThreadLocal.set(RemoteWebDriver(hubUrl, capability))
            }

            else -> throw NoSuchElementException(
                "Failed to create an instance of RemoteWebDriver for: " + testProperties.getProperty(
                    "browser"
                )
            )
        }
    }

    private fun isDriverExisting(): Boolean {
        val geckoDriver = File(ConstantFramework.DRIVER_DIRECTORY + "/geckodriver" + getExtension())
        val chromeDriver = File(ConstantFramework.DRIVER_DIRECTORY + "/chromedriver" + getExtension())
        return geckoDriver.exists() && chromeDriver.exists()
    }

    private fun downloadDriver() {
        if (!testProperties.getProperty("environment").toString().contains("headless-github")) {
            try {
                val process = when (getOperatingSystem()) {
                    "win" -> Runtime.getRuntime()
                        .exec("cmd.exe /c downloadDriver.sh", null, File(ConstantFramework.COMMON_RESOURCES))

                    "linux" -> Runtime.getRuntime()
                        .exec(arrayOf("sh", "-c", ConstantFramework.COMMON_RESOURCES + "/downloadDriver.sh"))

                    else -> Runtime.getRuntime()
                        .exec(arrayOf("/bin/sh -c ls", ConstantFramework.COMMON_RESOURCES + "/downloadDriver.sh"))
                }
                process.waitFor()
                BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                    reader.lines().forEach { log.debug(it) }
                }
            } catch (e: Exception) {
                log.error("Error downloading driver: ${e.message}")
            }
        }
    }

    private fun getOperatingSystem(): String {
        val os = System.getProperty("os.name", "generic").lowercase()
        return when {
            os.contains("mac") || os.contains("darwin") -> "mac"
            os.contains("win") -> "win"
            else -> "linux"
        }
    }

    private fun getExtension(): String {
        return if (getOperatingSystem().contains("win")) ".exe" else ""
    }
}
