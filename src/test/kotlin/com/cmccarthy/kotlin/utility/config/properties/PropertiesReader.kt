package com.cmccarthy.kotlin.utility.config.properties

import java.io.FileNotFoundException
import java.util.*

object PropertiesReader {
    private val frameworkProperties = loadProperties("properties/framework.properties")
    private val testProperties = loadProperties("properties/test.properties")

    private fun loadProperties(filename: String): Properties {
        val inputStream = PropertiesReader::class.java.classLoader.getResourceAsStream(filename)
            ?: throw FileNotFoundException("Resource $filename not found")
        return Properties().apply { load(inputStream) }
    }

    fun getPropertyFile(type: PropertyTypes): Properties {
        return when (type) {
            PropertyTypes.CONFIG -> frameworkProperties
            PropertyTypes.TEST -> testProperties
        }
    }
}
