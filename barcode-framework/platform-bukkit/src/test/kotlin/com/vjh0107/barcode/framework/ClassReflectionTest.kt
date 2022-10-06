package com.vjh0107.barcode.framework

import org.junit.jupiter.api.Test

class ClassReflectionTest {
    interface Testerface1
    interface Testerface2
    interface Testerface3
    class TestClass(val a: String, val b: Int) : Testerface1, Testerface2, Testerface3 {

    }

    @Test
    fun genericInterfaceTest() {
        val clazz: Class<*> = TestClass::class.java
        clazz.genericInterfaces.forEach {
            println(it.typeName)
        }
        if (clazz.genericInterfaces.contains(Testerface1::class.java)) {
            assert(true)
        } else {
            assert(false)
        }
    }

    @Test
    fun constructorTest() {
        val clazz = TestClass::class.java
        try {
            clazz.getConstructor().newInstance()
        } catch (exception: NoSuchMethodException) {
            assert(true)
        }
    }
}