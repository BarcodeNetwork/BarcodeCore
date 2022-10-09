plugins {
    kotlin("jvm")
    java
    `maven-publish`
    id("com.google.devtools.ksp")
    id("com.vjh0107.barcode.buildscripts.ksp-extension")
}

version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.vjh0107"
            artifactId = "barcodeframework-bukkit-common"
            version = project.version.toString()

            from(components["java"])
        }
    }
}

dependencies {
    compileOnly(Dependency.Minecraft.SPIGOT_REMAPPED) {
        this.exclude("com.google.gson")
    }
    implementationModule(BarcodeModule.Framework.COMMON)
    implementationModule(BarcodeModule.Framework.KOIN)
    compileOnly(Dependency.Minecraft.KyoriAdventure.API)
    implementation(Dependency.KotlinX.Serialization.JSON)
    with(Dependency.Koin) {
        compileOnly(CORE)
        compileOnly(ANNOTATIONS)
        ksp(KSP_COMPILER)
    }

    testImplementation(Dependency.Minecraft.SPIGOT_REMAPPED)
    testImplementationAll(Dependency.KOTEST)
    testImplementationModule(BarcodeModule.Framework.COMMON)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
