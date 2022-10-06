plugins {
    kotlin("jvm")
    java
    id("com.github.johnrengelman.shadow")
    id("com.google.devtools.ksp")
}

version = "1.0.0"

dependencies {
    compileOnly(Dependency.Minecraft.PAPER)
    compileOnly(Dependency.Plugin.CommandAPI)
    with(Dependency.Koin) {
        compileOnly(CORE)
        compileOnly(ANNOTATIONS)
    }

    compileOnly(jar(Dependency.Directory.CrazyAdvancementsAPI))
    compileOnly(jar(Dependency.Directory.JSEngine))

    compileOnlyModule(BarcodeModule.Framework.BUKKIT)
    compileOnlyModule(BarcodeModule.Framework.COMMON)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}