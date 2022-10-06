package com.vjh0107.barcode.framework.component.handler

import com.vjh0107.barcode.framework.AbstractBarcodeApplication
import com.vjh0107.barcode.framework.component.IBarcodeComponent
import com.vjh0107.barcode.framework.koin.bean.BarcodeBeanModuleFactory
import com.vjh0107.barcode.framework.koin.injector.ReflectionInjector
import java.io.File
import java.net.URL
import kotlin.reflect.KClass

abstract class AbstractKtorComponentHandler<A : AbstractBarcodeApplication, T : IBarcodeComponent>(
    val application: A
) : AbstractComponentHandler<T>() {
    final override fun findTargetClasses(): MutableSet<Class<*>> {
        val classes = mutableSetOf<Class<*>>()
        var name = getRootPackage()
        if (!name.startsWith("/")) {
            name = "/$name"
        }
        name = name.replace('.', '/')

        val url: URL = application::class.java.getResource(name)
            ?: throw NullPointerException("애플리케이션의 class 들을 불러오지 못하였습니다.")
        val directory = File(url.file)

        if (directory.exists()) {
            directory.walk()
                .filter { f -> f.isFile && !f.name.contains('$') && f.name.endsWith(".class") }
                .forEach {
                    val fullyQualifiedClassName = getRootPackage() + it.canonicalPath
                        .removePrefix(directory.canonicalPath)
                        .replace('/', '.')
                        .removeSuffix(".class")
                    try {
                        val clazz = Class.forName(fullyQualifiedClassName)
                        classes.add(clazz)
                    } catch (exception: ClassNotFoundException) {
                        System.err.println(exception)
                    }
                }
        }
        return classes
    }

    private fun getRootPackage(): String {
        return application::class.java.packageName
    }


    override fun createInstance(clazz: KClass<T>): T {
        val instance = ReflectionInjector.createInstance(clazz)
        BarcodeBeanModuleFactory.tryCreateBeanModule(clazz, instance)
        return instance
    }
}