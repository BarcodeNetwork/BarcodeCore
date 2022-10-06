plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    java
    application
}

description = "BarcodeNetwork API server application"

application {
    mainClass.set("com.vjh0107.barcode.api.BarcodeAPIApplicationKt")
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

repositories {
    mavenCentral()
}

dependencies {
    implementationAll(Dependency.Ktor.SERVER)
    implementation(Dependency.KotlinX.Serialization.JSON)
    implementation(Dependency.KotlinX.Coroutines.CORE)
    implementation(Dependency.Library.Logger.LOGBACK_CLASSIC)
    implementation(Dependency.Library.Logger.SLF4J_JDK14)

    with(Dependency.Koin) {
        implementation(CORE)
        implementation(KTOR)
        implementation(ANNOTATIONS)
        ksp(KSP_COMPILER)
    }

    implementationAll(Dependency.EXPOSED)
    implementation(Dependency.Library.MYSQL_CONNECTOR)
    implementation(Dependency.Library.HIKARICP)


    implementationModule(BarcodeModule.Bukkit.Modules.DATABASE)
    implementationModule(BarcodeModule.Framework.DATABASE)
    implementationModule(BarcodeModule.Framework.COMMON)
    implementationModule(BarcodeModule.Framework.KOIN)
    implementationModule(BarcodeModule.Framework.KTOR)

    testImplementation(Dependency.KotlinX.Coroutines.TEST)
    testImplementation(Dependency.Library.KOTLIN_TEST)
    testImplementation(Dependency.Ktor.SERVER_TEST)
    testImplementation(Dependency.Koin.TEST)
}