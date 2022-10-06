plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("dependency-manager") {
            id = "barcode-dependency-manager"
            implementationClass = "com.vjh0107.barcode.buildscripts.dependencymanager.DependencyManagerPlugin"
        }
    }
}