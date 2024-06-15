package com.cmccarthy.kotlin.utility.annotations

import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactoryFinder

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@PageFactoryFinder(
    FindBy.FindByBuilder::class
)
annotation class PageObject
