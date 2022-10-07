plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.md-5:SpecialSource:1.11.0")
    implementation(project(":modules:common"))
}

version = "1.0.0"
group = "com.vjh0107"

gradlePlugin {
    plugins.register("special-source") {
        displayName = "BarcodeSpecialSource"
        description = "Spigot SpecialSource gradle plugin for BarcodeNetwork"
        id = "com.vjh0107.barcode.buildscripts.special-source"
        implementationClass = "com.vjh0107.barcode.buildscripts.specialsource.SpecialSourcePlugin"
    }
}

pluginBundle {
    val projectUrl: String by project
    website = projectUrl
    vcsUrl = projectUrl
    description = project.description
    tags = listOf("barcode", "special-source")
}
