package com.vjh0107.barcode

import com.vjh0107.barcode.core.meister.data.MeisterParameter
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar
import com.vjh0107.barcode.framework.utils.formatters.colorize
import com.vjh0107.barcode.framework.utils.print
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.reflect.full.functions

class KotlinTest {
    object A {
        val a = print("A called")
    }
    @Test
    fun nothing() {
        val clazz = A::class.java
        clazz.kotlin.objectInstance
        clazz.kotlin.objectInstance
        clazz.kotlin.objectInstance
        clazz.kotlin.objectInstance

    }

    @Test
    fun calcTest() {
        val increase: Double = 1.2
        val level: Int = 5
        MeisterParameter.of(3.0, 0.14).getDisplay(50).print()
    }

    @Test
    fun onEachTest() {
        listOf(
            "",
            "&7현재 afadf &7개의 포인트가 투자되어 있습니다.",
            "&7우클릭시 투자된 모든 포인트를 회수합니다.",
            "",
            "&e◆ 보유중인 전문기술 스킬 초기화 포인트 : &6adgdgasg"
        ).map { it.colorize() }.print()
    }

    @Test
    fun whenTest() {
        val type = 1
        when(type) {
            1 -> { assert(true) }
            else -> { assert(false)}
        }
    }

    @Test
    fun reflectionTest() {
        val registrable = object : BarcodeRegistrable {
            override val id: String = ""

            @Registrar
            fun a() {
                println("called")
            }
        }

        val registrar = registrable::class.functions.forEach { kFunction ->
            kFunction.annotations.forEach { annotation ->
                if (annotation is Registrar) {
                    kFunction.call(registrable)
                }
            }
        }
    }

    @Test
    fun classTest() {
        val a = "AA"
        val b = "BB"
        val c = 3
        if (a.javaClass == c.javaClass) {
            println("fdsa")
            assert(false)
            return
        }
        if (a.javaClass == b.javaClass) {
            println("asdf")
            assert(true)
            return
        }
        assert(false)
    }

    data class ExampleDataObject(val a: String, val b: Long)

    @Test
    fun linkedTest() {
        val linkedHashSet: MutableList<ExampleDataObject> = mutableListOf()
        linkedHashSet.add(ExampleDataObject("a", LocalDateTime.now().toLocalTime().toNanoOfDay()))
        linkedHashSet.add(ExampleDataObject("b", LocalDateTime.now().toLocalTime().toNanoOfDay()))
        linkedHashSet.add(ExampleDataObject("c", LocalDateTime.now().toLocalTime().toNanoOfDay()))
        linkedHashSet.print()
    }
}