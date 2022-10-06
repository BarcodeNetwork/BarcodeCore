plugins {
    kotlin("jvm")
    java
    kotlin("plugin.serialization")
    `maven-publish`
}

group = "com.vjh0107"
version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.vjh0107"
            artifactId = "barcodeframework-database"
            version = project.version.toString()

            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(Dependency.KotlinX.Coroutines.CORE)
    compileOnly(Dependency.KotlinX.Serialization.JSON)
    compileOnly(Dependency.Library.HIKARICP)
    compileOnlyModule(BarcodeModule.Framework.COMMON)
    compileOnlyAll(Dependency.EXPOSED)

    testImplementation(Dependency.KotlinX.Coroutines.TEST)
    testImplementationAll(Dependency.KOTEST)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}