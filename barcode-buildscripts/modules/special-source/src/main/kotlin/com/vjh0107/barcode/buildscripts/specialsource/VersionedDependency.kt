package com.vjh0107.barcode.buildscripts.specialsource

interface VersionedDependency {
    val path: (version: String) -> String
}