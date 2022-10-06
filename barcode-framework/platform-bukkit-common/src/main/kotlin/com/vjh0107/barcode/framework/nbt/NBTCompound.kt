package com.vjh0107.barcode.framework.nbt

interface NBTCompound {
    fun hasTag(path: String): Boolean

    operator fun get(path: String): Any?

    fun getString(path: String): String

    fun getBoolean(path: String): Boolean

    fun getDouble(path: String): Double

    fun getInt(path: String): Int

    fun getNBTCompound(path: String): NBTCompound

    fun getTags(): Set<String>

    fun getTypeId(path: String): Int
}