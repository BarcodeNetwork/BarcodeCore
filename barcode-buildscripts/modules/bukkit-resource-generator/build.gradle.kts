plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3") {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.3")
    implementation(project(":modules:common"))
}


gradlePlugin {
    plugins {
        register("bukkit-resource-generator") {
            id = "barcode-buildscripts.bukkit-resource-generator"
            implementationClass = "com.vjh0107.barcode.buildscripts.resourcegenerator.BukkitResourceGeneratorPlugin"
        }
    }
}
