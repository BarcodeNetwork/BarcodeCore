package com.vjh0107.barcode.buildscripts.kspextension

import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

fun NamedDomainObjectContainer<KotlinSourceSet>.main(action: KotlinSourceSet.() -> Unit) {
    named("main").configure(action)
}

fun NamedDomainObjectContainer<KotlinSourceSet>.test(action: KotlinSourceSet.() -> Unit) {
    named("test").configure(action)
}
