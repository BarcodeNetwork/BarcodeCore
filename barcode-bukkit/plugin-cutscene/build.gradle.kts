plugins {
    kotlin("jvm")
    java
    id("com.github.johnrengelman.shadow")
    id("net.minecrell.plugin-yml.bukkit")
    kotlin("plugin.serialization")
}

bukkit {
    name = "BarcodeCutscene"
    version = "1.0.0"
    main = "com.vjh0107.barcode.cutscene.BarcodeCutscenePlugin"
    author = "vjh0107"
    apiVersion = "1.18"
    depend = listOf("CommandAPI", "BarcodeFramework")
}

group = "com.vjh0107"
version = "1.0.0"

dependencies {
    compileOnly(Dependency.Minecraft.PAPER_API)
    compileOnly(Dependency.Minecraft.AUTH_LIB)
    compileOnly(Dependency.Minecraft.DATA_FIXER)
    compileOnly(Dependency.Minecraft.BRIGADIER)
    compileOnly(Dependency.Minecraft.SPIGOT_REMAPPED)

    compileOnly(Dependency.Plugin.CommandAPI)
    compileOnly(Dependency.KotlinX.Serialization.JSON)
    compileOnlyAll(Dependency.Ktor.CLIENT)
    compileOnly(Dependency.KotlinX.Coroutines.CORE)
    compileOnly(Dependency.Plugin.ProtocolLib)

    compileOnlyModule(BarcodeModule.Framework.BUKKIT)
    compileOnlyModule(BarcodeModule.Framework.COMMON)
    compileOnlyModule(BarcodeModule.Framework.DATABASE)

    testImplementationAll(Dependency.Ktor.CLIENT)
    testImplementationAll(Dependency.KOTEST)
    testImplementation(Dependency.KotlinX.Coroutines.TEST)
    testImplementation(Dependency.KotlinX.Serialization.JSON)
    testImplementation(Dependency.Minecraft.AUTH_LIB)
    testImplementation(Dependency.Minecraft.MOCK_BUKKIT)
    testImplementationModule(BarcodeModule.Framework.BUKKIT)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.shadowJar {
    this.excludeKotlin()
    this.excludeJetbrains()
    this.destinationDirectory.set(file("../build_outputs"))
    this.archiveFileName.set("barcodecore-cutscene.jar")
    this.dependShadowJar(BarcodeModule.Framework.BUKKIT)
}