import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

val kotlinVersion = loadProperties(rootProject.gradle.parent?.rootProject?.projectDir?.path + "/gradle.properties").getProperty("kotlinVersion")

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}

gradlePlugin {
    plugins {
        register("ksp-extension") {
            id = "barcode-buildscripts.ksp-extension"
            implementationClass = "com.vjh0107.barcode.buildscripts.kspextension.KSPExtensionPlugin"
        }
    }
}