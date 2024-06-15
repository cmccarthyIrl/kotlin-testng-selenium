package com.cmccarthy.kotlin.utility.extent

import com.aventstack.extentreports.AnalysisStrategy
import com.aventstack.extentreports.ExtentReports
import com.aventstack.extentreports.reporter.ExtentSparkReporter
import com.aventstack.extentreports.reporter.configuration.Theme
import com.aventstack.extentreports.reporter.configuration.ViewName
import com.cmccarthy.kotlin.utility.config.properties.PropertiesReader
import com.cmccarthy.kotlin.utility.config.properties.PropertyTypes
import java.util.*

object SparkReporter {
    @JvmField
    val extent: ExtentReports = GetReporter.extent

    private object GetReporter {
        val extent: ExtentReports = ExtentReports()
        private val spark: ExtentSparkReporter = ExtentSparkReporter("target/spark-reports/spark-report.html")
        private val frameworkProperties = PropertiesReader.getPropertyFile(PropertyTypes.CONFIG)
        private val testProperties = PropertiesReader.getPropertyFile(PropertyTypes.TEST)

        init {
            spark.config().theme = Theme.STANDARD
            spark.config().reportName = "Coles App Test Report"
            spark.config().documentTitle = "Coles App Automation"
            spark.config().encoding = "UTF-8"
            spark.config().isTimelineEnabled = true
            spark.config().enableOfflineMode(false)
            spark.config().thumbnailForBase64(true)
            spark.config().timeStampFormat = "MMM dd, yyyy HH:mm:ss"
            spark.viewConfigurer().viewOrder().`as`(arrayOf(ViewName.DASHBOARD, ViewName.TEST)).apply()
            extent.attachReporter(spark)
            extent.setAnalysisStrategy(AnalysisStrategy.TEST)
            extent.setSystemInfo("Operating System", "")
            extent.setSystemInfo(
                "Environment",
                testProperties.getProperty("environment").uppercase(Locale.getDefault())
            )
            extent.setSystemInfo("Base URL", "")
            extent.setSystemInfo("Test Type", "App")
            extent.setSystemInfo("Platform", "")
            extent.setSystemInfo("Platform Version", "")
            extent.setSystemInfo("Version", "")

        }
    }
}