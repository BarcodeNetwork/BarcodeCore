plugins {
    kotlin("jvm")
    java
    id("com.vjh0107.barcode.buildscripts.special-source")
    `maven-publish`
}

version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.vjh0107"
            artifactId = "barcodeframework-bukkit-v1_19_R1"
            version = project.version.toString()

            from(components["java"])
        }
    }
}

barcodeTasks {
    archiveTask = tasks.shadowJar

    specialSource {
        version.set("1.19.2")
        archiveTask.set(tasks.shadowJar)
        enabled.set(true)
    }
}

dependencies {
    compileOnly(Dependency.Minecraft.SPIGOT_REMAPPED)
    compileOnly(Dependency.Minecraft.PAPER_API)
    compileOnly(Dependency.Minecraft.KyoriAdventure.API)
    compileOnly(Dependency.Minecraft.KyoriAdventure.BUKKIT)

    implementationModule(BarcodeModule.Framework.COMMON)
    implementationModule(BarcodeModule.Framework.BUKKIT_COMMON)

    testImplementationAll(Dependency.KOTEST)
    testImplementationModule(BarcodeModule.Framework.COMMON)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
