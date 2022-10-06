plugins {
    // kotlin("jvm")
    java
    id("com.github.johnrengelman.shadow")
    kotlin("jvm")
}

group = "io.lumine"
version = "1.1.6"

repositories {
    mavenCentral()
    // HolographicDisplays
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://nexus.sparky.ac/repository/Sparky/")
    maven("https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.18.2-R0.1-SNAPSHOT")
    compileOnly(Dependency.Minecraft.AUTH_LIB)
    compileOnly(Dependency.Minecraft.BRIGADIER)

    compileOnly(Dependency.Plugin.CitizensAPI)
    compileOnly(Dependency.Plugin.PAPI)
    compileOnly(Dependency.Plugin.HolographicDisplays)
    compileOnly("org.projectlombok:lombok:1.18.20")

    compileOnly("com.google.code.findbugs:jsr305:3.0.0")

    compileOnly(fileTree("libs"))
    implementation(fileTree("../lumineutils"))
}
tasks.shadowJar {
    archiveFileName.set("barcodecore-mythiclib-1.1.6-all.jar")
    this.destinationDirectory.set(file("../build_outputs"))
    this.relocate("io.lumine.utils", "io.lumine.mythic.utils")
}