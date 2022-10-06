package com.vjh0107.barcode.framework

import io.kotest.core.spec.style.AnnotationSpec

class InheritanceTest : AnnotationSpec() {
    abstract class AbstractAbstractTestClass {
        init {
            println("AbstractAbstractTestClassInit")
        }
    }
    abstract class AbstractTestClass : AbstractAbstractTestClass() {
        init {
            println("AbstractTestClassInit")
        }
        fun init() {
            println(this.javaClass.name)
        }
    }

    class TestClass : AbstractTestClass() {
        init {
            println("TestClassInit")
        }
    }

    @Test
    fun test() {
        TestClass().init()
    }
}