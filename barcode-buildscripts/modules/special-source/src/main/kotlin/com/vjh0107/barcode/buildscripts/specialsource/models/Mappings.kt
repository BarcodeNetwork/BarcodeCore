package com.vjh0107.barcode.buildscripts.specialsource.models

import com.vjh0107.barcode.buildscripts.specialsource.VersionedDependency

enum class Mappings(override val path: (version: String) -> String) : VersionedDependency {
    MOJANG({"org.spigotmc:minecraft-server:$it-R0.1-SNAPSHOT:maps-mojang@txt"}),
    SPIGOT({"org.spigotmc:minecraft-server:$it-R0.1-SNAPSHOT:maps-spigot@csrg"});
}