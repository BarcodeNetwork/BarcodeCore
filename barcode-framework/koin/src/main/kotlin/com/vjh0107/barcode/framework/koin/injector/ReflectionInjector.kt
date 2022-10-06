package com.vjh0107.barcode.framework.koin.injector

import com.vjh0107.barcode.framework.koin.annotation.KoinAnnotationProcessor
import com.vjh0107.barcode.framework.koin.getKoin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.indexKey
import org.koin.core.instance.InstanceContext
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

@OptIn(KoinInternalApi::class)
object ReflectionInjector {
    /**
     * constructor 에 koin 을 이용하여 인스턴스를 주입하여 타겟 인스턴스를 생성합니다.
     * 어노테이션은 primitive 만 지원하기 때문에 Koin 의 ParametersHolder 의 parameter 를 지원하지 않습니다.
     * providedInstance 에서 제공하는 인스턴스를 주입할 타겟 클래스의 파라미터가 superclass 여도 캐스팅 후 주입됩니다.
     *
     * @param klass 생성할 클래스
     * @param providedInstanceMap koin 빈에서가 아닌 따로 주입할 파라미터들
     */
    fun <T : Any> createInstance(klass: KClass<T>, providedInstanceMap: Map<KClass<*>, *>? = null): T {
        val constructors = klass.constructors.first()
        val parameters = constructors.parameters.map { parameter ->
            val parameterKClass = parameter.type.classifier as KClass<*>

            if (providedInstanceMap?.filter { (it.key::isSuperclassOf)(parameterKClass) }?.isNotEmpty() == true) {
                return@map providedInstanceMap[parameterKClass]
            }

            val qualifier = KoinAnnotationProcessor.getQualifier(parameter)
            val scopeQualifier = KoinAnnotationProcessor.getScopeQualifier(klass)
            val indexKey = indexKey(parameterKClass, qualifier, scopeQualifier)

            val koin = getKoin()
            val factory = koin.instanceRegistry.instances[indexKey]
            val defaultContext = InstanceContext(koin, koin.getScope(scopeQualifier.value))
            factory?.get(defaultContext)
        }.toTypedArray()

        return constructors.call(*parameters)
    }
}