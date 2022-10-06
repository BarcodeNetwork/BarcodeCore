plugins {
    kotlin("jvm")
    java
    `maven-publish`
    id("com.google.devtools.ksp")
}

group = "com.vjh0107"
version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.vjh0107"
            artifactId = "barcodeframework-google"
            version = project.version.toString()

            from(components["java"])
        }
    }
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}

dependencies {
    compileOnly(Dependency.KotlinX.Coroutines.CORE)
    compileOnlyModule(BarcodeModule.Framework.COMMON)
    with(Dependency.Koin) {
        implementation(CORE)
        implementation(ANNOTATIONS)
        ksp(KSP_COMPILER)
    }

    implementationAll(Dependency.GOOGLE_SHEETS)

    testImplementation(Dependency.Koin.TEST)
    testImplementation(Dependency.KotlinX.Coroutines.TEST)
    testImplementationAll(Dependency.KOTEST)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}