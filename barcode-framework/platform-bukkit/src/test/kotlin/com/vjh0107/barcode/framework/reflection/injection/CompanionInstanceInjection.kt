package com.vjh0107.barcode.framework.reflection.injection

import com.vjh0107.barcode.framework.injection.instance.injector.InjectorFactory
import com.vjh0107.barcode.framework.reflection.injection.models.ExampleClassWithCompanionObject
import com.vjh0107.barcode.framework.reflection.injection.models.ExampleComponent
import com.vjh0107.barcode.framework.utils.print
import org.junit.jupiter.api.Test
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredMemberProperties

class CompanionInstanceInjection {
    @Test
    fun injection() {
        val exampleClass = ExampleClassWithCompanionObject::class.java.getDeclaredConstructor().newInstance()
        exampleClass::class.companionObject!!.declaredMemberProperties.forEach {
            if (it is KMutableProperty<*>) {
                it.setter.parameters.forEach {
                    it.print()
                }
                it.setter.call(exampleClass::class.companionObjectInstance, exampleClass)
            }
        }
        ExampleClassWithCompanionObject.instance.testPrintAA()
    }

    @Test
    fun validateObject() {
        val exampleClass = ExampleClassWithCompanionObject::class.java.getDeclaredConstructor().newInstance()
        val companionObject = exampleClass::class.companionObjectInstance ?: throw Exception()
        if (companionObject is CompanionInstanceProvider<*>) {
            val instanceProperty = exampleClass::class.companionObject!!.declaredMemberProperties.find {
                it.name == CompanionInstanceProvider::class.declaredMemberProperties.first().name
            }
            if (instanceProperty is KMutableProperty<*>) {
                instanceProperty.setter.call(exampleClass::class.companionObjectInstance, exampleClass)
            }
        }
        ExampleClassWithCompanionObject.instance.testPrintAA()
    }

    @Test
    fun productionTest() {
        val instance = ExampleComponent::class.java.getDeclaredConstructor().newInstance()
        InjectorFactory.getComponentInstanceInjector().inject(instance)
        ExampleComponent.instance.printAA()
        assert(ExampleComponent.instance.getResult())
    }
}