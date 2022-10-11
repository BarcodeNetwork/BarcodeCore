plugins {
    kotlin("jvm")
    java
    id("com.vjh0107.bukkit-resource-generator")
    id("com.vjh0107.special-source")
    id("com.vjh0107.bukkit-executor")
    id("com.vjh0107.ksp-extension")
    id("com.google.devtools.ksp")

    id("com.github.johnrengelman.shadow")
    kotlin("plugin.serialization")
    `maven-publish`
}

version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.vjh0107"
            artifactId = "barcodeframework-core"
            version = project.version.toString()

            from(components["java"])
        }
    }
}

barcodeTasks {
    archiveTask = tasks.shadowJar

    bukkitResource {
        main = "com.vjh0107.barcode.framework.BarcodeFrameworkPlugin"
        name = "BarcodeFramework"
        apiVersion = "1.18"
        author = "vjh0107"
        softDepend = listOf(
            "Vault",
            "HolographicDisplays",
            "PlaceholderAPI"
        )
    }
    specialSource {
        version.set("1.18.2")
        archiveTask.set(tasks.shadowJar)
        enabled.set(true)
    }
    bukkitExecutor {
        enabled.set(true)
        archiveTask.set(tasks.shadowJar)
    }
}

dependencies {
    compileOnly(Dependency.Minecraft.PAPER_API)
    compileOnly(Dependency.Minecraft.SPIGOT_REMAPPED)
    compileOnly(Dependency.Library.NETTY)
    compileOnly(Dependency.Minecraft.AUTH_LIB)
    with(Dependency.Plugin) {
        compileOnly(PAPI)
        compileOnly(Vault)
        compileOnly(HolographicDisplays)
    }
    implementation(Dependency.Plugin.CommandAPI)
    implementation(Dependency.KotlinX.Coroutines.CORE)
    implementation(Dependency.KotlinX.Serialization.JSON)
    implementationAll(Dependency.Ktor.CLIENT)
    implementationAll(Dependency.EXPOSED)
    with(Dependency.Koin) {
        implementation(CORE)
        implementation(ANNOTATIONS)
        ksp(KSP_COMPILER)
    }
    with(Dependency.Library) {
        implementation(KOTLIN_REFLECT)
        implementation(MYSQL_CONNECTOR)
        implementation(HIKARICP)
        implementation(SQLITE)
    }
    implementationModule(BarcodeModule.Framework.KOIN)
    implementationModule(BarcodeModule.Framework.DATABASE)
    implementationModule(BarcodeModule.Framework.COMMON)
    implementationModule(BarcodeModule.Framework.GOOGLE_SHEETS)
    implementationModule(BarcodeModule.Framework.BUKKIT_COMMON)
    implementationModule(BarcodeModule.Framework.BUKKIT_V1_19_R1)

    testImplementation(Dependency.Minecraft.PAPER_API)
    testImplementation(Dependency.KotlinX.Coroutines.TEST)
    testImplementation(Dependency.Library.MOCKK)
    testImplementationAll(Dependency.KOTEST)
    testImplementation(Dependency.Koin.TEST)
    testImplementationModule(BarcodeModule.Framework.COMMON)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.shadowJar {
    this.relocate("dev.jorel.commandapi", "com.vjh0107.barcode.commandapi")
    setBuildOutputDir()
    this.archiveFileName.set("barcodecore-barcodeFramework.jar")
    includeAll(Dependency.GOOGLE_SHEETS)
}
