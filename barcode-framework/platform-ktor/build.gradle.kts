plugins {
    kotlin("jvm")
    java
    `maven-publish`
    id("com.google.devtools.ksp")
    id("com.vjh0107.ksp-extension")
    kotlin("plugin.serialization")
}

group = "com.vjh0107"
version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.vjh0107"
            artifactId = "barcodeframework-ktor"
            version = project.version.toString()

            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementationModule(BarcodeModule.Framework.COMMON)
    with(Dependency.Koin) {
        implementation(CORE)
        implementation(ANNOTATIONS)
        ksp(KSP_COMPILER)
        testImplementation(TEST)
    }
    implementation(Dependency.KotlinX.Coroutines.CORE)
    implementationModule(BarcodeModule.Framework.KOIN)
    implementationModule(BarcodeModule.Framework.DATABASE)
    implementation(Dependency.Library.HIKARICP)

    implementation(Dependency.Library.Logger.LOGBACK_CLASSIC)
    implementation(Dependency.Library.Logger.SLF4J_JDK14)
    implementationAll(Dependency.Ktor.SERVER)
    implementation(Dependency.KotlinX.Serialization.JSON)

    implementation(Dependency.Library.MYSQL_CONNECTOR)
    implementationAll(Dependency.EXPOSED)
    testImplementation(Dependency.KotlinX.Coroutines.TEST)
    testImplementationAll(Dependency.KOTEST)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}