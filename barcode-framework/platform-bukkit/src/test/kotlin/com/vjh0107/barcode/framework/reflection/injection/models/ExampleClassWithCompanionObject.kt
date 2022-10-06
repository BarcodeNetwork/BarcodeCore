package com.vjh0107.barcode.framework.reflection.injection.models

import com.vjh0107.barcode.framework.reflection.injection.CompanionInstanceProvider

class ExampleClassWithCompanionObject {
    companion object : CompanionInstanceProvider<ExampleClassWithCompanionObject> {
        override lateinit var instance: ExampleClassWithCompanionObject

        val a = "1"
        fun testPrintBB() {
            println("printedBB")
        }
    }

    fun testPrintAA() {
        println("printedAA")
    }
}