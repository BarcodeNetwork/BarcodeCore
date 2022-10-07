plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3") {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.3")
    implementation(project(":modules:common"))
}

version = "1.0.0"
group = "com.vjh0107"

gradlePlugin {
    plugins {
        register("bukkit-resource-generator") {
            displayName = "BarcodeBukkitResourceGenerator"
            description = "Bukkit resource auto generator plugin for BarcodeNetwork"
            id = "com.vjh0107.barcode.buildscripts.bukkit-resource-generator"
            implementationClass = "com.vjh0107.barcode.buildscripts.resourcegenerator.BukkitResourceGeneratorPlugin"
        }
    }
}

pluginBundle {
    val projectUrl: String by project
    website = projectUrl
    vcsUrl = projectUrl
    description = project.description
    tags = listOf("barcode", "bukkit-resource-generator")
}