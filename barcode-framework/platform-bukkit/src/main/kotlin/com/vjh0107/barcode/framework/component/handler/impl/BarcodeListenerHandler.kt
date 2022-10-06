package com.vjh0107.barcode.framework.component.handler.impl

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.handler.AbstractBukkitComponentHandler
import com.vjh0107.barcode.framework.component.BarcodeListener
import com.vjh0107.barcode.framework.component.handler.BarcodeComponentHandler
import org.bukkit.event.HandlerList
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import kotlin.reflect.KClass

@BarcodeComponentHandler
@Factory(binds = [AbstractBukkitComponentHandler::class])
@Named("BarcodeListenerHandler")
class BarcodeListenerHandler<P: AbstractBarcodePlugin>(
    plugin: P
) : AbstractBukkitComponentHandler<P, BarcodeListener>(plugin) {

    override fun processAnnotation(clazz: KClass<BarcodeListener>) {
        if (clazz.java.genericInterfaces.contains(BarcodeListener::class.java)) {
            registerComponent(clazz)
        }
    }

    override fun onPostEnable() {
        this.getComponents().forEach {
            plugin.registerListener(it)
        }
    }

    override fun onDisable() {
        HandlerList.unregisterAll(plugin)
        plugin.logger.info("모든 ${plugin.name} 의 Listener 를 unregister 합니다.")
    }
}