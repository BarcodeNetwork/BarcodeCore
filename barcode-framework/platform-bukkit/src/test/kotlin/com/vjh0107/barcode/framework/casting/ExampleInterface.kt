package com.vjh0107.barcode.framework.casting

interface ExampleInterface<T> {
    fun printType() {
        print(this::class.java.typeName)
    }
}