package com.vjh0107.barcode.framework.component.handler

import com.vjh0107.barcode.framework.AbstractBarcodeApplication
import com.vjh0107.barcode.framework.koin.getMappings
import com.vjh0107.barcode.framework.utils.uncheckedNonnullCast
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Module
@ComponentScan
class KtorComponentHandlerModule : ComponentHandlerModule {
    @Factory
    fun provideKtorComponentHandlers(application: AbstractBarcodeApplication): ComponentHandlers {
        val componentHandlers = getComponentHandlers(module)
            .map { it.constructors.first().call(application) }
            .toList()

        return object : ComponentHandlers {
            override fun get(): List<ComponentHandler> = componentHandlers
        }
    }
}