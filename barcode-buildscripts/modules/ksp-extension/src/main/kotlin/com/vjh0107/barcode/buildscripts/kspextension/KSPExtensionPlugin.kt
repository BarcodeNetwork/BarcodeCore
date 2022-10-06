package com.vjh0107.barcode.buildscripts.kspextension

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

/**
 * KSP 에 대한 설정을 자동으로 해줍니다.
 */
class KSPExtensionPlugin : Plugin<Project> {
    companion object {
        private const val KOTLIN = "org.jetbrains.kotlin:kotlin-jvm"
    }
    override fun apply(target: Project) {
        with(target) {
            if (!pluginManager.hasPlugin(KOTLIN)) {
                project.pluginManager.apply(KotlinPlatformJvmPlugin::class)
            }

            val configure: KotlinJvmProjectExtension.() -> Unit = {
                sourceSets.main {
                    kotlin.srcDir("build/generated/ksp/main/kotlin")
                }
                sourceSets.test {
                    kotlin.srcDir("build/generated/ksp/test/kotlin")
                }
            }
            target.extensions.configure<KotlinJvmProjectExtension>("kotlin", configure)
        }
    }
}