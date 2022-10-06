package com.vjh0107.barcode.framework

interface BarcodeApplication : LoggerProvider {
    fun onPreLoad() {}
    fun onLoad() {}
    fun onPostLoad() {}

    fun onPreEnable() {}
    fun onEnable() {}
    fun onPostEnable() {}

    fun onPreDisable() {}
    fun onDisable() {}
    fun onPostDisable() {}
}