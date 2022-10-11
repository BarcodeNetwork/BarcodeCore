package com.vjh0107.barcode.framework.component.handler

import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.IBarcodeComponent
import com.vjh0107.barcode.framework.component.Reloadable
import com.vjh0107.barcode.framework.exceptions.ConstructorNotAllowedException
import com.vjh0107.barcode.framework.utils.print
import com.vjh0107.barcode.framework.utils.uncheckedCast
import com.vjh0107.barcode.framework.utils.uncheckedNonnullCast
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass

abstract class AbstractComponentHandler<T : IBarcodeComponent> : ComponentHandler {
    private val componentClasses: MutableSet<KClass<T>> = mutableSetOf()
    private val instances: MutableSet<T> = mutableSetOf()

    /**
     * 컴포넌트를 register 합니다.
     */
    fun registerComponent(clazz: KClass<T>) {
        componentClasses.add(clazz)
    }

    /**
     * 등록되어 인스턴스화되어 있는 컴포넌트들을 전부 가져옵니다.
     */
    fun getComponents(): Set<T> {
        return instances
    }

    /**
     * 컴포넌트들을 검색할 scope 를 구합니다.
     */
    abstract fun findTargetClasses(): MutableSet<Class<*>>

    /**
     * 어노테이션을 프로세싱하여 componentClasses 에 등록합니다.
     */
    abstract fun processAnnotation(clazz: KClass<T>)

    /**
     * 컴포넌트 클래스를 인자로 받아 인스턴스를 생성합니다.
     */
    abstract fun createInstance(clazz: KClass<T>): T

    /**
     * 인스턴스가 생성된 후
     */
    open fun onInstanceCreated(instance: T) {}

    // #3
    /**
     * enable 되고난 후
     */
    abstract fun onPostEnable()

    // #1
    private fun processAnnotations() {
        val classes: Set<Class<*>> = findTargetClasses()

        classes.forEach clazz@{ clazz ->
            for (annotation in clazz.annotations) {
                if (annotation is BarcodeComponent) {
                    val klass = Reflection.createKotlinClass(clazz).uncheckedCast<KClass<T>>() ?: return@clazz
                    processAnnotation(klass)
                }
            }
        }
    }

    // #2
    private fun createInstances() {
        try {
            componentClasses.forEach clazz@{ clazz ->
                val instance = createInstance(clazz)
                onInstanceCreated(instance)
                instances.add(instance)
            }
        } catch (exception: NoSuchMethodException) {
            exception.printStackTrace()
            throw ConstructorNotAllowedException()
        }
    }

    // #1 -> #2 -> #3
    final override fun onEnable() {
        // 1. 어노테이션이 달린 클래스들을 클래스 컬렉션에 담는다.
        processAnnotations()
        // 2. 그 클래스들을 기반으로 인스턴스를 생성하여 인스턴스 컬렉션에 담는다.
        createInstances()
        // 3. 그 인스턴스 컬렉션을 통해 행해질 process 들이 담긴 abstract method 를 호출한다.
        onPostEnable()
    }

    override fun onReload() {
        instances.run {
            forEach {
                if (it is Reloadable) {
                    it.close()
                }
            }
            forEach {
                if (it is Reloadable) {
                    it.load()
                }
            }
        }
    }
}