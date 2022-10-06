plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.md-5:SpecialSource:1.11.0")
    implementation(project(":modules:common"))
}

gradlePlugin {
    plugins.register("special-source") {
        id = "barcode-buildscripts.special-source"
        implementationClass = "com.vjh0107.barcode.buildscripts.specialsource.SpecialSourcePlugin"
    }
}
