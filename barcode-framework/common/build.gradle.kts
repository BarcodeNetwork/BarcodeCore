plugins {
    kotlin("jvm")
    java
    `maven-publish`
}

group = "com.vjh0107"
version = "1.0.0"

dependencies {
    implementation(Dependency.KotlinX.Serialization.JSON)
    implementation(Dependency.Library.KOTLIN_REFLECT)
    testImplementationAll(Dependency.KOTEST)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.vjh0107"
            artifactId = "barcodeframework-common"
            version = project.version.toString()

            from(components["java"])
        }
    }
}