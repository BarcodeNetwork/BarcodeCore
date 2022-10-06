package com.vjh0107.barcode.framework.component

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.handler.BukkitComponentHandlerModule
import com.vjh0107.barcode.framework.component.handler.ComponentHandlers
import com.vjh0107.barcode.framework.koin.injector.inject
import com.vjh0107.barcode.framework.utils.print
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.ksp.generated.module
import org.koin.test.KoinTest

class ComponentTest : AnnotationSpec(), KoinTest {
    @Test
    fun test() = runTest {
        val module = BukkitComponentHandlerModule().module

        startKoin {
            modules(module)
        }

        val plugin = mockk<AbstractBarcodePlugin>()

        val componentHandlers: ComponentHandlers by inject { parametersOf(plugin) }

        componentHandlers.get().forEach {
            it.print()
        }
        componentHandlers.get().size.print()
    }

    @After
    fun teardown() {
        stopKoin()
    }
}