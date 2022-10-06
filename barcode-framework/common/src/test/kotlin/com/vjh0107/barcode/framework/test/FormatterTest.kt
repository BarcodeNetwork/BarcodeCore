package com.vjh0107.barcode.framework.test

import com.vjh0107.barcode.framework.utils.formatters.toBarcodeFormat
import com.vjh0107.barcode.framework.utils.print
import com.vjh0107.barcode.framework.utils.round
import com.vjh0107.barcode.framework.utils.uncheckedNonnullCast
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class FormatterTest : AnnotationSpec() {
    @Test
    fun print() {
        val elements = listOf("[희귀] 아이템1", "[고급] 아이템2", "[상급] 아이템3")
        elements.toBarcodeFormat(2, "및 <remain>개").print().contains("1개") shouldBe true
        elements.toBarcodeFormat(2, "등 <all>개").print().contains("3개") shouldBe true
    }

    @Test
    fun roundTest() {
        val double = 1.125512
        round(double, 3).print() shouldBe 1.126
    }

    @Test
    fun castTest() {
        val number: Any = 1.12412514521
        number.uncheckedNonnullCast<Double>().print()
    }
}