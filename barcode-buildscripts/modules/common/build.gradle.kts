plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins.register("barcode-common") {
        id = "com.vjh0107.barcode.buildscripts.common"
        implementationClass = "com.vjh0107.barcode.buildscripts.common.CommonPlugin"
        version = "1.0.0"
    }
}
