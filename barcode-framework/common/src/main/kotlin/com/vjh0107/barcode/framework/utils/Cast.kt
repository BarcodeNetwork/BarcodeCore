package com.vjh0107.barcode.framework.utils

@Suppress("UNCHECKED_CAST")
fun <T> Any?.uncheckedNonnullCast(): T {
    return this as T
}

@Suppress("UNCHECKED_CAST")
fun <T> Any?.uncheckedCast(): T? {
    return this as T?
}