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
            description = "Bukkit Executor Plugin for BarcodeNetwork"
            id = "com.vjh0107.barcode-buildscripts.bukkit-executor"
            implementationClass = "com.vjh0107.barcode.buildscripts.bukkitexecutor.BukkitExecutorPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/BarcodeNetwork/BarcodeCore"
    vcsUrl = "https://github.com/BarcodeNetwork/BarcodeCore"
    description = project.description
    tags = listOf("bukkit-executor")
}

tasks.login {
    val key = "xCtIJaM4XSPvc6KHL7FPp7L1HyV9njvf"
    val secret = "efMe7eIxw0KgawGC6Kfhlw6tSBApsbGj"
    if (key == null || secret == null) {
        throw GradleException("gradlePublishKey 혹은 gradlePublishSecret 이 정의되지 않았습니다.")
    }
    System.setProperty("gradle.publish.key", key)
    System.setProperty("gradle.publish.secret", secret)
    login()
}