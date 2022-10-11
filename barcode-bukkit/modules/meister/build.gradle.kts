plugins {
    kotlin("jvm")
    java
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.vjh0107.ksp-extension")
}

group = "com.vjh0107.barcode"
version = "1.0.0"

dependencies {
    compileOnly(Dependency.Minecraft.PAPER_API)
    compileOnlyAll(Dependency.EXPOSED)
    compileOnly(Dependency.KotlinX.Serialization.JSON)
    compileOnly(Dependency.KotlinX.Coroutines.CORE)

    compileOnlyModule(BarcodeModule.Framework.DATABASE)
    compileOnlyModule(BarcodeModule.Framework.COMMON)
    compileOnlyModule(BarcodeModule.Framework.BUKKIT)
    compileOnlyModule(BarcodeModule.Framework.KOIN)
    compileOnlyModule(BarcodeModule.Bukkit.Modules.DATABASE)
    compileOnly(Dependency.Plugin.CommandAPI)
    compileOnly(Dependency.Plugin.BetonQuest)
    compileOnly(Dependency.Plugin.MythicMobs)

    with(Dependency.Koin) {
        compileOnly(CORE)
        compileOnly(ANNOTATIONS)
        ksp(KSP_COMPILER)
    }
}
