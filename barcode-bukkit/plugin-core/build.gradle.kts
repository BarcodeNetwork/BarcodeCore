plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("net.minecrell.plugin-yml.bukkit")
    id("com.github.johnrengelman.shadow")
}

group = "com.vjh0107"
version = "0.6.0"

val dependencyPlugins = listOf(
    "BarcodeFramework",
    "ProtocolLib",
    "PlaceholderAPI",
    "MagicSpells",
    "MythicMobs",
    "MMOCore",
    "MMOItems",
    "BetonQuest",
    "TAB",
    "GPS",
    "Skript",
    "BarcodeCutscene",
    "CommandAPI",
    "Sentinel",
    "Citizens",
    "ModelEngine",
)

repositories {
    maven("https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    compileOnly(Dependency.Minecraft.PAPER_API)

    compileOnly(Dependency.KotlinX.Coroutines.CORE)
    compileOnly(Dependency.KotlinX.Serialization.JSON)
    compileOnlyAll(Dependency.EXPOSED)

    compileOnlyTransitively(Dependency.Plugin.Vault)
    compileOnlyTransitively(Dependency.Plugin.ProtocolLib)
    compileOnly(Dependency.Plugin.PAPI)
    compileOnly(Dependency.Plugin.Skript)
    compileOnlyTransitively(Dependency.Plugin.BetonQuest)
    compileOnly(Dependency.Plugin.CommandAPI)
    compileOnly(Dependency.Plugin.Sentinel)
    compileOnly(Dependency.Plugin.Citizens)
    compileOnly(Dependency.Plugin.BigDoors)
    compileOnlyAll(Dependency.Plugin.TAB, isTransitive = true)
    compileOnly(Dependency.Plugin.MythicMobs)
    compileOnly(jar(Dependency.Directory.GPS))
    compileOnly(jar(Dependency.Directory.MythicLib))
    compileOnly(jar(Dependency.Directory.ModelEngine))
    compileOnly(jar(Dependency.Directory.MagicSpells))

    compileOnlyModule(BarcodeModule.MMO.CORE)
    compileOnlyModule(BarcodeModule.MMO.ITEMS)
    compileOnlyModule(BarcodeModule.Framework.BUKKIT)
    compileOnlyModule(BarcodeModule.Framework.DATABASE)
    compileOnlyModule(BarcodeModule.Framework.COMMON)
    // compileOnlyModule(BarcodeModule.CUTSCENE)
    compileOnlyModule(BarcodeModule.Bukkit.ADVANCEMENT)
    implementationModule(BarcodeModule.Bukkit.Modules.DATABASE)

    testImplementation(Dependency.Library.KOTLIN_REFLECT)
    testImplementation(Dependency.KotlinX.Serialization.JSON)
    testImplementation(Dependency.Minecraft.PAPER_API)
    testImplementation(Dependency.Minecraft.SPIGOT_API)
    testImplementationModule(BarcodeModule.Framework.COMMON)
    testImplementationModule(BarcodeModule.Framework.BUKKIT)
    testImplementationModule(BarcodeModule.MMO.ITEMS)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

bukkit {
    name = "BarcodeCore"
    main = "com.vjh0107.barcode.core.BarcodeCorePlugin"
    depend = dependencyPlugins
    author = "vjh0107"
    website = "http://github.com/vjh0107"
    apiVersion = "1.18"
}

tasks {
    getByName<Test>("test") {
        useJUnitPlatform()
    }

    compileKotlin {
        dependModuleTask(BarcodeModule.Bukkit.Modules.DATABASE, "compileKotlin")

        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime,kotlin.ExperimentalStdlibApi"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime,kotlin.ExperimentalStdlibApi"
    }

    shadowJar {
        dependShadowJar(BarcodeModule.Framework.BUKKIT)
        dependShadowJar(BarcodeModule.Bukkit.ADVANCEMENT)
        // dependShadowJar(BarcodeModule.CUTSCENE)
        excludeKotlin()
        excludeJetbrains()
        setBuildOutputDir()
        archiveFileName.set("barcode-bukkit.jar")
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(120, "seconds")
}
