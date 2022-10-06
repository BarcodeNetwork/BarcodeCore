@file:JvmName("KtorComponentHandlerModuleGen")
@file:JvmMultifileClass
package org.koin.ksp.generated

import org.koin.dsl.*


val com_vjh0107_barcode_framework_component_handler_KtorComponentHandlerModule = module {
	val moduleInstance = com.vjh0107.barcode.framework.component.handler.KtorComponentHandlerModule()
	factory(qualifier=null) { moduleInstance.provideKtorComponentHandlers(get()) } 
	factory(qualifier=org.koin.core.qualifier.StringQualifier("BarcodeRouterHandler")) { com.vjh0107.barcode.framework.component.handler.impl.BarcodeRouterHandler(get()) } bind(com.vjh0107.barcode.framework.component.handler.AbstractKtorComponentHandler::class)
}
val com.vjh0107.barcode.framework.component.handler.KtorComponentHandlerModule.module : org.koin.core.module.Module get() = com_vjh0107_barcode_framework_component_handler_KtorComponentHandlerModule