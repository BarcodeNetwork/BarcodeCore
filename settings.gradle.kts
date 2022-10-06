import org.gradle.internal.Cast.uncheckedCast

rootProject.name = "BarcodeCore"

pluginManagement {
    val kotlinVersion: String by settings
    val ktorVersion: String by settings
    val kspVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        kotlin("kapt") version kotlinVersion apply false
        id("io.ktor.plugin") version ktorVersion apply false
        id("com.google.devtools.ksp") version kspVersion apply false
    }

    repositories {
        gradlePluginPortal()
    }
}


file(rootProject.projectDir.path + "/publish.gradle.kts").let {
    if (it.exists()) {
        apply(it.path)
    }
}

includeBuild("barcode-dependency-manager")

include("barcodecore-cutscene")

include("barcodecore-mmo:mmocore")
include("barcodecore-mmo:mmoitems")
include("barcodecore-mmo:mythiclib")
include("barcodecore-clientblock")

apply(from = "barcode-buildscripts/scripts/module-management/module-includer.gradle.kts")
val includeAll = uncheckedCast<(target: String) -> Unit>(extra["includeAll"])
    ?: throw NoSuchMethodException("includeAll")
val includeAllExcept = uncheckedCast<(target: String, except: String) -> Unit>(extra["includeAllExcept"])
    ?: throw NoSuchMethodException("includeAllExcept")
includeAll("barcode-bukkit:modules")
includeAllExcept("barcode-bukkit", "modules")
includeAll("barcode-framework")

include("barcodeapi-server")