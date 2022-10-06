package com.vjh0107.barcode.framework.reflection

import com.vjh0107.barcode.framework.exceptions.ConstructorNotAllowedException
import com.vjh0107.barcode.framework.utils.print
import org.junit.jupiter.api.Test

class ConstructorTest {
    fun print() {
        "a".print()
    }

    @Test
    fun test() {
        val componentClasses = listOf(TestClassImpl1::class.java, TestClassImpl2::class.java)
        try {
            componentClasses.forEach clazz@{ clazz ->
                clazz.constructors.forEach {
                    if (it.parameters.size == 1 && it.parameters.first().type.isAssignableFrom(ConstructorTest::class.java)) {
                        val a = it.newInstance(this) as TestClassImpl2
                        a.print()
                        return@clazz
                    }
                }
                val instance = clazz.getConstructor().newInstance() as TestClassImpl1
                instance.print()
            }
        } catch (exception: NoSuchMethodException) {
            throw ConstructorNotAllowedException()
        }
    }
}

interface TestClass

class TestClassImpl1 : TestClass {
    fun print() {
        "baba".print()
    }
}

class TestClassImpl2(val a: ConstructorTest) : TestClass {
    fun print() {
        "abab".print()
        a.print()
    }
}

