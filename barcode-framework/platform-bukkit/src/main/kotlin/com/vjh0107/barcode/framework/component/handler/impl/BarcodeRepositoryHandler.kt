package com.vjh0107.barcode.framework.component.handler.impl

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeRepository
import com.vjh0107.barcode.framework.component.handler.AbstractBukkitComponentHandler
import com.vjh0107.barcode.framework.component.handler.BarcodeComponentHandler
import com.vjh0107.barcode.framework.component.handler.HandlerPriority
import kotlin.reflect.KClass

@BarcodeComponentHandler(priority = HandlerPriority.REPOSITORY)
class BarcodeRepositoryHandler<P: AbstractBarcodePlugin>(
    plugin: P
) : AbstractBukkitComponentHandler<P, BarcodeRepository>(plugin)  {
    override fun processAnnotation(clazz: KClass<BarcodeRepository>) {
        if (clazz.java.genericInterfaces.contains(BarcodeRepository::class.java)) {
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