package com.vjh0107.barcode.framework.component.handler

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.IBarcodeComponent
import com.vjh0107.barcode.framework.injection.instance.injector.InjectorFactory
import com.vjh0107.barcode.framework.koin.bean.BarcodeBeanModuleFactory
import com.vjh0107.barcode.framework.koin.injector.ReflectionInjector
import java.util.jar.JarFile
import kotlin.reflect.KClass

abstract class AbstractBukkitComponentHandler<P : AbstractBarcodePlugin, T : IBarcodeComponent>(
    val plugin: P
) : AbstractComponentHandler<T>() {
    final override fun findTargetClasses(): MutableSet<Class<*>> {
        val classes: MutableSet<Class<*>> = mutableSetOf()
        val entries = JarFile(plugin.file).entries()
        while (entries.hasMoreElements()) {
            val name = entries.nextElement().name.replace("/", ".")
            if (name.startsWith(getRootPackage()) && name.endsWith(".class")) {
                try {
                    val clazz = Class.forName(name.removeSuffix(".class"))
                    classes.add(clazz)
                } catch (exception: ClassNotFoundException) {
                    exception.printStackTrace()
                }
            }
        }
        return classes
    }

    private fun getRootPackage(): String {
        return plugin::class.java.packageName
    }

    override fun createInstance(clazz: KClass<T>): T {
        val provideInstance: Map<KClass<*>, *> = mapOf(Pair(plugin::class, plugin))
        val instance = ReflectionInjector.createInstance(clazz, provideInstance)
        BarcodeBeanModuleFactory.tryCreateBeanModule(clazz, instance)
        return instance
    }

    override fun onInstanceCreated(instance: T) {
        InjectorFactory.getComponentInstanceInjector().inject(instance)
    }
}