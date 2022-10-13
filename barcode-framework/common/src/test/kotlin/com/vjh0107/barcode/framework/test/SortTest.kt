package com.vjh0107.barcode.framework.test

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class SortTest : AnnotationSpec() {
    private val map = mutableMapOf<Int, String>()
    @Before
    fun init() {
        map[3] = "adf3"
        map[1] = "adf1"
        map[5] = "adf5"
        map[0] = "adf0"
    }

    @Test
    fun sort() {
        map.map { it.key }.first() shouldBe 3
        map.toSortedMap().map { it.key }.first() shouldBe 0
    }
}