plugins {
    kotlin("jvm")
    java
}

group = "net.Indyuce"
version = "1.8.2"

repositories {
    maven("https://nexus.sparky.ac/repository/Sparky/")
}

dependencies {
    compileOnly(Dependency.Minecraft.SPIGOT_API)
    compileOnly(Dependency.Plugin.PAPI)
    compileOnly("org.projectlombok:lombok:1.18.20")
    compileOnly("me.vagdedes.spartan:SpartanAPI:1.0")
    compileOnly("com.google.code.findbugs:jsr305:3.0.0")

    compileOnly(jar(Dependency.Directory.MythicLib))
    compileOnly(Dependency.Plugin.MythicMobs)

    compileOnly(Dependency.Minecraft.AUTH_LIB)

    compileOnlyModule(BarcodeModule.Framework.BUKKIT)
    compileOnlyModule(BarcodeModule.Framework.DATABASE)
    compileOnlyModule(BarcodeModule.Framework.COMMON)
}

tasks.jar {
    this.archiveFileName.set("barcodecore-mmocore-${archiveVersion.get()}.jar")
}