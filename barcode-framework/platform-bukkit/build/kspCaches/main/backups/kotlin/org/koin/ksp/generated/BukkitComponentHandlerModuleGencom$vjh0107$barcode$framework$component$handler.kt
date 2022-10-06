@file:JvmName("BukkitComponentHandlerModuleGen")
@file:JvmMultifileClass
package org.koin.ksp.generated

import org.koin.dsl.*


val com_vjh0107_barcode_framework_component_handler_BukkitComponentHandlerModule = module {
	val moduleInstance = com.vjh0107.barcode.framework.component.handler.BukkitComponentHandlerModule()
	factory(qualifier=null) { moduleInstance.provideBukkitComponentHandlers(get()) } 
	factory(qualifier=org.koin.core.qualifier.StringQualifier("BarcodeCommandHandler")) { com.vjh0107.barcode.framework.component.handler.impl.BarcodeCommandHandler(get()) } bind(com.vjh0107.barcode.framework.component.handler.AbstractBukkitComponentHandler::class)
	factory(qualifier=org.koin.core.qualifier.StringQualifier("BarcodeListenerHandler")) { com.vjh0107.barcode.framework.component.handler.impl.BarcodeListenerHandler(get()) } bind(com.vjh0107.barcode.framework.component.handler.AbstractBukkitComponentHandler::class)
	factory(qualifier=org.koin.core.qualifier.StringQualifier("BarcodePluginManagerHandler")) { com.vjh0107.barcode.framework.component.handler.impl.BarcodePluginManagerHandler(get()) } bind(com.vjh0107.barcode.framework.component.handler.AbstractBukkitComponentHandler::class)
	factory(qualifier=org.koin.core.qualifier.StringQualifier("BarcodeRegistrableHandler")) { com.vjh0107.barcode.framework.component.handler.impl.registrable.BarcodeRegistrableHandler(get()) } bind(com.vjh0107.barcode.framework.component.handler.AbstractBukkitComponentHandler::class)
}
val com.vjh0107.barcode.framework.component.handler.BukkitComponentHandlerModule.module : org.koin.core.module.Module get() = com_vjh0107_barcode_framework_component_handler_BukkitComponentHandlerModule