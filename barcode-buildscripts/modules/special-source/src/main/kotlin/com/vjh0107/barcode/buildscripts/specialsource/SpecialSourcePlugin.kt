package com.vjh0107.barcode.buildscripts.specialsource

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create

class SpecialSourcePlugin : Plugin<Project> {
    companion object {
        private const val SHADOW = "com.github.johnrengelman.shadow"
    }

    override fun apply(project: Project) {
        with(project) project@{
            if (!pluginManager.hasPlugin(SHADOW)) {
                project.pluginManager.apply(SHADOW)
            }

            val extension = extensions.create<SpecialSourceExtension>("barcodeSpecialSource")
            val task = tasks.register("obfuscateFromSpecialSource", SpecialSourceTask::class.java)

            task.configure task@{
                this.isEnabled = extension.enabled.getOrElse(true)
                this.version.set(extension.version)
                this.archiveTask.set(extension.archiveTask)
            }

            tasks.getByName("shadowJar").finalizedBy(task)
        }
    }
}