package com.vjh0107.barcode.framework.component.handler.impl

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.handler.AbstractBukkitComponentHandler
import com.vjh0107.barcode.framework.component.BarcodeKoinModule
import com.vjh0107.barcode.framework.component.handler.BarcodeComponentHandler
import com.vjh0107.barcode.framework.component.handler.HandlerPriority
import com.vjh0107.barcode.framework.koin.getKoin
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import kotlin.reflect.KClass

@BarcodeComponentHandler(priority = HandlerPriority.PROVIDER)
@Factory(binds = [AbstractBukkitComponentHandler::class])
@Named("BarcodeKoinModuleHandler")
class BarcodeKoinModuleHandler<P: AbstractBarcodePlugin>(
    plugin: P
) : AbstractBukkitComponentHandler<P, BarcodeKoinModule>(plugin) {

    // TODO: clazz.java 를 코틀린 클래스로
    override fun processAnnotation(clazz: KClass<BarcodeKoinModule>) {
        if (clazz.java.genericInterfaces.contains(BarcodeKoinModule::class.java)) {
            registerComponent(clazz)
        }
    }

    override fun onPostEnable() {
        this.getComponents().forEach {
            getKoin().loadModules(listOf(it.targetModule))
        }
    }

    override fun onDisable() {
        this.getComponents().forEach {
            getKoin().unloadModules(listOf(it.targetModule))
        }
    }
}