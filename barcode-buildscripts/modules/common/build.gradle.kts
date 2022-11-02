plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins.register("barcode-common") {
        id = "com.vjh0107.barcode.buildscripts.common"
        implementationClass = "com.vjh0107.barcode.buildscripts.common.CommonPlugin"
        version = project.version.toString()
    }
}

group = "com.vjh0107"
version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.vjh0107.barcode"
            artifactId = "buildscripts-common"
            version = project.version.toString()

            from(components["java"])
        }
    }
}