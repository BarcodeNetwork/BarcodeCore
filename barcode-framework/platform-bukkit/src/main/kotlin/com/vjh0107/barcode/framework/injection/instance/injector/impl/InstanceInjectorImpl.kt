package com.vjh0107.barcode.framework.injection.instance.injector.impl

import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider
import com.vjh0107.barcode.framework.injection.instance.injector.InstanceInjector
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

class InstanceInjectorImpl<T> : InstanceInjector<T> {
    override fun inject(target: T) {
        val targetNotNull = target ?: return
        val companionObject = targetNotNull::class.companionObject ?: return
        if (companionObject.findAnnotation<InjectInstance>() == null) {
            return
        }
        val companionObjectInstance = targetNotNull::class.companionObjectInstance
        if (companionObjectInstance is InstanceProvider<*>) {
            val instanceProperty = companionObject.declaredMemberProperties.find {
                it.name == "instance"
            }
            if (instanceProperty is KMutableProperty<*>) {
                instanceProperty.setter.call(targetNotNull::class.companionObjectInstance, target)
            }
        }
    }
}