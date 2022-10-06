package com.vjh0107.barcode.framework.component.handler

import com.vjh0107.barcode.framework.koin.getMappings
import com.vjh0107.barcode.framework.utils.uncheckedNonnullCast
import org.koin.core.module.Module
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

interface ComponentHandlerModule {
    fun getComponentHandlers(module: Module): List<KClass<ComponentHandler>> {
        return module
            .getMappings()
            .map { it.value.beanDefinition.primaryType }
            .filter { type -> type.hasAnnotation<BarcodeComponentHandler>() }
            .associateBy { it.findAnnotation<BarcodeComponentHandler>()!!.priority.slot }
            .toSortedMap(compareBy { it })
            .map { it.value }
            .toList()
            .uncheckedNonnullCast()
    }
}