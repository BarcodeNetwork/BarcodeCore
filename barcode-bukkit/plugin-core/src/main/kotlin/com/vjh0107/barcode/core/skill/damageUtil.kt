package com.vjh0107.barcode.core.skill

fun isCalledByAllowedDamageSources(): Boolean {
    val elements = Thread.currentThread().stackTrace
    for (element in elements) {
        if (element.className.startsWith("com.nisovin.magicspells.")) return true
        // mythiclib damage meta & mythicmobs
        if (element.className.startsWith("io.lumine.")) return true
    }
    return false
}
fun isCalledByMeisterBehavior(): Boolean {
    val elements = Thread.currentThread().stackTrace
    for (element in elements) {
        if (element.className.startsWith("com.vjh0107.barcode.core.meister")) return true
    }
    return false
}