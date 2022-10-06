package com.vjh0107.barcode.framework.test

import io.kotest.core.spec.style.AnnotationSpec

class InheritanceTest : AnnotationSpec() {
    abstract class AbstractAbstractTestClass {
        init {
            println("AbstractAbstractTestClassInit")
        }

        abstract fun print()
    }

    abstract class AbstractTestClass : AbstractAbstractTestClass() {
        init {
            println("AbstractTestClassInit")
            classes.add(this.javaClass)
        }
    }


    class TestClass : AbstractTestClass() {
        override fun print() {
            print(1)
        }
    }

    class TestClass2 : AbstractTestClass() {
        override fun print() {
            print(2 + 1231)
        }
    }

    @Test
    fun test() {
        TestClass()
        TestClass2()
        classes.forEach {
            println(it.name)
            println("asdf")
        }
    }

    companion object {
        val classes: MutableSet<Class<out AbstractAbstractTestClass>> = mutableSetOf()
    }
}