package com.vjh0107.barcode.framework.koin.test.injector

import com.vjh0107.barcode.framework.koin.annotation.BarcodeSingleton
import com.vjh0107.barcode.framework.koin.bean.BarcodeBeanModuleFactory
import com.vjh0107.barcode.framework.koin.injector.ReflectionInjector
import com.vjh0107.barcode.framework.koin.injector.inject
import com.vjh0107.barcode.framework.utils.print
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class ReflectionInjectorTest : AnnotationSpec() {
    @BarcodeSingleton(binds = [TestInterface::class])
    class TestClass(val arg: TestArgInterface): TestInterface, TestInterface2 {
        override fun get(): Int {
            return arg.a
        }

        override fun getString(): String {
            return arg.a.toString()
        }
    }

    @BarcodeSingleton(binds = [TestInterface::class])
    class TestClass2(val arg: TestArgClass): TestInterface, TestInterface2 {
        override fun get(): Int {
            return arg.a
        }

        override fun getString(): String {
            return arg.a.toString()
        }
    }


    interface TestInterface {
        fun get(): Int
    }

    interface TestInterface2 {
        fun getString(): String
    }

    class TestArgClass(arg1: Int): TestArgInterface {
        override val a = arg1
    }

    interface TestArgInterface {
        val a: Int
    }

    private lateinit var instance: TestClass
    private lateinit var instance2: TestClass
    private lateinit var instance3: TestClass2

    @Before
    fun init() {
        startKoin {  }
        val testArg: TestArgInterface = TestArgClass(10)
        // 파라미터가 AbstractBarcodePlugin 이고, AbstractBarcodePlugin 인스턴스를 주입
        instance = ReflectionInjector.createInstance(TestClass::class, mapOf(Pair(TestArgInterface::class, testArg)))
        BarcodeBeanModuleFactory.tryCreateBeanModule(TestClass::class, instance)
        val testArg2: TestArgClass = TestArgClass(20)
        // 파라미터가 AbstractBarcodePlugin 이고, 구현된 BarcodePlugin 을 주입
        instance2 = ReflectionInjector.createInstance(TestClass::class, mapOf(Pair(TestArgInterface::class, testArg2)))
        val testArg3: TestArgClass = TestArgClass(30)
        // 파라미터가 구현된 BarcodePlugin 이고, 구현된 BarcodePlugin 을 주입
        instance3 = ReflectionInjector.createInstance(TestClass2::class, mapOf(Pair(TestArgClass::class, testArg3)))
        assert(true)
    }

    @Test
    fun injectTest() {
        instance.get() shouldBe 10
    }

    @Test
    fun createModuleTest() {
        val testInterface: TestInterface by inject()
        testInterface.get().print() shouldBe 10
        try {
            val testInterface2: TestInterface2 by inject()
            testInterface2.getString().print()
            assert(false)
        } catch (exception: Exception) {
            assert(true)
        }
    }

    @After
    fun teardown() {
        stopKoin()
    }
}