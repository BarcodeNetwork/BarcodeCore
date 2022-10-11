plugins {
    kotlin("jvm")
    java
    id("net.minecrell.plugin-yml.bukkit")
    id("com.github.johnrengelman.shadow")
    kotlin("plugin.serialization")
}

group = "com.vjh0107"
version = "1.0.0"

bukkit {
    main = "com.vjh0107.barcode.clientblock.BarcodeClientBlockPlugin"
    apiVersion = "1.18"

    name = "BarcodeClientBlock"
    author = "vjh0107"
    depend = listOf(
        "CommandAPI",
        "BarcodeFramework",
        "LuckPerms",
        "ProtocolLib"
    )
}

dependencies {
    compileOnly(Dependency.Minecraft.PAPER_API)
    compileOnlyModule(BarcodeModule.Framework.BUKKIT)
    compileOnlyModule(BarcodeModule.Framework.COMMON)
    compileOnlyModule(BarcodeModule.Framework.DATABASE)

    compileOnly(Dependency.KotlinX.Coroutines.CORE)
    compileOnly(Dependency.KotlinX.Serialization.JSON)
    compileOnlyAll(Dependency.EXPOSED)

    compileOnly(Dependency.Plugin.CommandAPI)
    compileOnly(Dependency.Plugin.LuckPermsAPI)
    compileOnly(Dependency.Plugin.ProtocolLib)
}

tasks.shadowJar {
    excludeKotlin()
    excludeJetbrains()
    dependShadowJar(BarcodeModule.Framework.BUKKIT)
    destinationDirectory.set(file("../build_outputs/"))
    archiveFileName.set("barcodecore-clientblock.jar")
}