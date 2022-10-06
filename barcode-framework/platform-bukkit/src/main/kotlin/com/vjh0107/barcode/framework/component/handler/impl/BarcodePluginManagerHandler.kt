package com.vjh0107.barcode.framework.component.handler.impl

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.handler.AbstractBukkitComponentHandler
import com.vjh0107.barcode.framework.component.BarcodePluginManager
import com.vjh0107.barcode.framework.component.handler.BarcodeComponentHandler
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import kotlin.reflect.KClass

@BarcodeComponentHandler
@Factory(binds = [AbstractBukkitComponentHandler::class])
@Named("BarcodePluginManagerHandler")
class BarcodePluginManagerHandler<P: AbstractBarcodePlugin>(
    plugin: P
) : AbstractBukkitComponentHandler<P, BarcodePluginManager>(plugin) {

    override fun processAnnotation(clazz: KClass<BarcodePluginManager>) {
        if (clazz.java.genericInterfaces.contains(BarcodePluginManager::class.java)) {
            registerComponent(clazz)
        }
    }

    override fun onPostEnable() {
        this.getComponents().forEach {
            it.load()
        }
    }

    override fun onDisable() {
        this.getComponents().run {
            forEach { it.close() }
        }
    }
}