package com.vjh0107.barcode.buildscripts.specialsource.models

import com.vjh0107.barcode.buildscripts.specialsource.VersionedDependency

enum class Spigots(override val path: (version: String) -> String) : VersionedDependency {
    REMAPPED_MOJANG({"org.spigotmc:spigot:$it-R0.1-SNAPSHOT:remapped-mojang"}),
    REMAPPED_OBF({"org.spigotmc:spigot:$it-R0.1-SNAPSHOT:remapped-obf"})
}