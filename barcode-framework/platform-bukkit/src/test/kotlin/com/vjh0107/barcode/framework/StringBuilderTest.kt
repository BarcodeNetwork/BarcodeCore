package com.vjh0107.barcode.framework

import org.junit.jupiter.api.Test

class StringBuilderTest {
    @Test
    fun test() {
        assert(getRootPackage() == "com.vjh0107.barcode")
    }

    private fun getRootPackage() : String {
        return this.javaClass.packageName.split(".").dropLast(1).synthesize()
    }

    private fun <T> Iterable<T>.synthesize(): String {
        return this.joinTo(StringBuilder(), ".").toString()
    }
}