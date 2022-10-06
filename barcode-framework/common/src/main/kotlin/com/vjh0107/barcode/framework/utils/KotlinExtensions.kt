package com.vjh0107.barcode.framework.utils

fun <T> Collection<T>.print() {
    this.forEach {
        println(it)
    }
}

fun String.print(): String {
    println(this)
    return this
}

fun <T> T.print(): T = also { println(it) }
fun <T> T.print(prefix: String): T = also { println(prefix + it) }
fun <T> T.print(prefix: String, suffix: String): T = also { println(prefix + it + suffix) }

fun Any?.isNull(): Boolean {
    return this == null
}

inline fun <reified T> runIfTypeIs(instance: Any?, block: T.() -> Unit) {
    if (instance is T) {
        block(instance)
    }
}