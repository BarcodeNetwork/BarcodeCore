package com.vjh0107.barcode.framework.component.handler

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.koin.getMappings
import com.vjh0107.barcode.framework.utils.uncheckedNonnullCast
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.ksp.generated.module
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Module
@ComponentScan
class BukkitComponentHandlerModule : ComponentHandlerModule {
    /**
     * plugin 을 inject 하지 않습니다. 파라미터와 함께 inject 해주세요.
     * component handler 들을 인스턴스화해서 제공합니다.
     */
    @Factory
    fun <T : AbstractBarcodePlugin>provideBukkitComponentHandlers(plugin: T): ComponentHandlers {
        val componentHandlers = getComponentHandlers(module)
            .map { it.constructors.first().call(plugin) }
            .toList()

        return object : ComponentHandlers {
            override fun get(): List<ComponentHandler> = componentHandlers
        }
    }
}