plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":modules:common"))
}

version = "1.0.0"
group = "com.vjh0107"

gradlePlugin {
    plugins {
        register("bukkit-executor") {
            displayName = "BarcodeBukkitExecutor"
            description = "Bukkit executor plugin for BarcodeNetwork"
            id = "com.vjh0107.barcode.buildscripts.bukkit-executor"
            implementationClass = "com.vjh0107.barcode.buildscripts.bukkitexecutor.BukkitExecutorPlugin"
        }
    }
}

pluginBundle {
    val projectUrl: String by project
    website = projectUrl
    vcsUrl = projectUrl
    description = project.description
    tags = listOf("barcode", "bukkit-executor")
}