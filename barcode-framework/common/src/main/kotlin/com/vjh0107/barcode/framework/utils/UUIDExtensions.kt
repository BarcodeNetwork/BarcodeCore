package com.vjh0107.barcode.framework.utils

import java.util.*
import kotlin.jvm.Throws

@Throws(IllegalArgumentException::class)
fun String.toUUID(): UUID {
    return if (this.contains("-")) {
        UUID.fromString(this)
    } else {
        UUID.fromString(transformWithDashes(this))
    }
}

private val UUID_REGEX = "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)".toRegex()

private fun transformWithDashes(string: String): String {
    return string.replace(UUID_REGEX, "$1-$2-$3-$4-$5")
}