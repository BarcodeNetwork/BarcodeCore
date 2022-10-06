package com.vjh0107.barcode.buildscripts.dependencymanager

import org.gradle.api.Plugin
import org.gradle.api.Project

class DependencyManagerPlugin : Plugin<Project> {
    companion object {
        lateinit var project: Project
    }

    override fun apply(project: Project) {
        Companion.project = project
    }
}