plugins {
    id("barcode-dependency-manager")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

subprojects {
    repositories {
        // MavenCentral & JitPack & jcenter
        mavenCentral()
        mavenLocal()

        maven("https://www.jitpack.io") {

        }
        jcenter()

        // WorldEdit & WorldGuard
        maven("https://maven.enginehub.org/repo/")

        // Paper
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.destroystokyo.com/repository/maven-public/")

        // Spigot
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

        // Minecraft
        maven("https://libraries.minecraft.net/")

        // Citizens
        maven("https://repo.citizensnpcs.co/")

        // PlaceholderAPI
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

        // ProtocolLib
        maven("https://repo.dmulloy2.net/nexus/repository/public/")

        // TAB
        maven("https://repo.kryptonmc.org/releases")

        // https://stackoverflow.com/questions/41586289/how-to-ignore-bad-pom-inconsistent-module-descriptor-version
        // BetonQuest
        maven("https://betonquest.org/nexus/repository/betonquest/").metadataSources {
            artifact()
        }

        // BigDoors (Opener)
        maven("https://eldonexus.de/repository/maven-public/")

        // oss sonatype
        maven("https://oss.sonatype.org/content/repositories/snapshots/")

        // bukkit plugin ?
        maven("https://repo.codemc.io/repository/maven-public/")

        // lumine team repository
        maven("https://mvn.lumine.io/repository/maven-public/")

        // jetbrains ktor
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
        }
    }
}
