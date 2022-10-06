package com.vjh0107.barcode.framework

import com.vjh0107.barcode.framework.utils.print
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class KotlinTest : AnnotationSpec() {
    @Test
    fun calcTest() {
        1.1 * 2 shouldBe 2.2
    }

    @Test
    fun packageNameTest() {
        this.javaClass.packageName.print() shouldBe "com.vjh0107.barcode.framework"
    }

    @Test
    fun test() {
        val classes = listOf(ExampleClass1::class, ExampleClass2::class)

        classes.forEach {
            it.constructors.size.print()
        }
    }


    interface ExampleClass

    class ExampleClass1(val a: String) : ExampleClass {

    }

    class ExampleClass2() : ExampleClass {

    }

    @Test
    fun splitTest() {
        val string = "a\nb\nc"
        val list = string.split("\n")
        list.size.print()
        assert(list.size == 3)
    }

    @Test
    fun collectionTest() {
        val list = mutableListOf<String>()
        list[2] = "1"
    }

    @Test
    fun intToByteTest() {
        val target = 4
        target.toByte().print()
    }
}