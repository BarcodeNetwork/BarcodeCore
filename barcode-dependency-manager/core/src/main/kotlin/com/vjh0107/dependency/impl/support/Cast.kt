package com.vjh0107.dependency.impl.support

@Suppress("unchecked_cast", "nothing_to_inline")
internal inline fun <T> uncheckedCast(obj: Any?): T {
    return obj as T
}
