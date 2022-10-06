plugins {
    kotlin("jvm")
    java
    kotlin("plugin.serialization")
}

group = "com.vjh0107.barcode"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnlyAll(Dependency.EXPOSED)
    compileOnlyModule(BarcodeModule.Framework.DATABASE)
    compileOnlyModule(BarcodeModule.Framework.COMMON)
    compileOnlyModule(BarcodeModule.Framework.BUKKIT)
    compileOnly(Dependency.KotlinX.Serialization.JSON)
}
