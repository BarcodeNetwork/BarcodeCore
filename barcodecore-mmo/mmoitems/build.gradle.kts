plugins {
    kotlin("jvm")
    java
}

group = "net.Indyuce"
version = "6.6.4"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://nexus.sparky.ac/repository/Sparky/")
}

dependencies {
    val transitive = Action<ExternalModuleDependency> { isTransitive = false }

    compileOnlyModule(BarcodeModule.MMO.CORE)
    compileOnly("net.citizensnpcs:citizens-main:2.0.28-SNAPSHOT")
    compileOnly("com.mojang:authlib:1.5.21")
    compileOnly("me.clip:placeholderapi:2.10.10")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7", transitive)
    compileOnly("org.projectlombok:lombok:1.18.20")
    compileOnly("com.github.InventivetalentDev:GlowAPI:1.5.1-SNAPSHOT")
    compileOnly("com.google.code.findbugs:jsr305:3.0.0")
    compileOnly(Dependency.Minecraft.SPIGOT_API)
    compileOnly("io.papermc:paperlib:1.0.5")
    compileOnly(fileTree("libs"))
    compileOnly(jar(Dependency.Directory.MythicLib))
}

tasks.jar {
    this.archiveFileName.set("barcodecore-mmoitems-${archiveVersion.get()}.jar")
}