plugins {
    kotlin("jvm")
    java
    `maven-publish`
}

group = "com.vjh0107"
version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.vjh0107"
            artifactId = "barcodeframework-koin"
            version = project.version.toString()

            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Dependency.KotlinX.Coroutines.CORE)
    implementationModule(BarcodeModule.Framework.COMMON)
    with(Dependency.Koin) {
        implementation(CORE)
        implementation(ANNOTATIONS)
    }
    implementation(Dependency.Library.KOTLIN_REFLECT)

    testImplementation(Dependency.Koin.TEST)
    testImplementation(Dependency.KotlinX.Coroutines.TEST)
    testImplementationAll(Dependency.KOTEST)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}