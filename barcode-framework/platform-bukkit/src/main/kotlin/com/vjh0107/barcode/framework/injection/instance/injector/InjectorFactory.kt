package com.vjh0107.barcode.framework.injection.instance.injector

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.IBarcodeComponent
import com.vjh0107.barcode.framework.injection.instance.injector.impl.InstanceInjectorImpl

object InjectorFactory {
    private val pluginInstanceInjector = InstanceInjectorImpl<AbstractBarcodePlugin>()
    private val componentInstanceInjector = InstanceInjectorImpl<IBarcodeComponent>()

    fun getPluginInstanceInjector(): InstanceInjector<AbstractBarcodePlugin> {
        return pluginInstanceInjector
    }

    fun getComponentInstanceInjector(): InstanceInjector<IBarcodeComponent> {
        return componentInstanceInjector
    }
}