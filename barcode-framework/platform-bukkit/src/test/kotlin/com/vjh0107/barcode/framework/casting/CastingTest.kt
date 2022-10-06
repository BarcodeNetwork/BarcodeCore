package com.vjh0107.barcode.framework.casting

import com.vjh0107.barcode.framework.utils.runIfTypeIs
import org.junit.jupiter.api.BeforeAll

class CastingTest {
    companion object {
        private val list = mutableListOf<ExampleInterface<*>>()

        private val object1 = object : ExampleInterface<String> {}
        private val object2 = object : ExampleInterface<Double> {}

        @BeforeAll
        @JvmStatic
        fun initialize() {
            list.add(object1)
            list.add(object2)
        }
    }

    // @Test
    fun failed() {
        list.forEach {
            val objectWhat = it as ExampleInterface<String>
            objectWhat.printType()
        }
        // fail
    }

    // @Test
    fun failed2() {
        runIfTypeIs<ExampleInterface<String>>(list[1]) {
            println("is String")
        }
        runIfTypeIs<ExampleInterface<Double>>(list[1]) {
            println("is Double")
        }
    }
}