package com.vjh0107.barcode.framework.test.component

import io.kotest.core.spec.style.AnnotationSpec
import kotlinx.coroutines.test.runTest

class ApplicationTest : AnnotationSpec() {
    @Test
    fun applicationTest() = runTest {

        val application = TestKtorApplication()
        application.build().start(wait = true)
    }
}