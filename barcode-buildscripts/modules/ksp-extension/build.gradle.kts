import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
}

repositories {
    mavenCentral()
}

val kotlinVersion = loadProperties(rootProject.gradle.parent?.rootProject?.projectDir?.path + "/gradle.properties").getProperty("kotlinVersion")

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}

version = "1.0.0"
group = "com.vjh0107"

gradlePlugin {
    plugins {
        register("ksp-extension") {
            displayName = "KSPExtension"
            description = "KSP extension plugin"
            id = "com.vjh0107.ksp-extension"
            implementationClass = "com.vjh0107.barcode.buildscripts.kspextension.KSPExtensionPlugin"
        }
    }
}

pluginBundle {
    val projectUrl: String by project
    website = projectUrl
    vcsUrl = projectUrl
    description = project.description
    tags = listOf("barcode", "ksp-extension")
}

