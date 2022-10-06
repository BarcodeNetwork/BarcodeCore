package com.vjh0107.barcode.framework.koin.bean

import com.vjh0107.barcode.framework.koin.annotation.KoinAnnotationProcessor
import com.vjh0107.barcode.framework.koin.getKoin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Kind
import org.koin.core.definition.indexKey
import org.koin.core.instance.FactoryInstanceFactory
import org.koin.core.instance.InstanceFactory
import org.koin.core.instance.SingleInstanceFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass

@OptIn(KoinInternalApi::class)
@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
object BarcodeBeanModuleFactory {
    /**
     * 모듈을 생성합니다. 만약 아무런 타입의 Bean 도 아닐 경우, 생성되지 않습니다.
     */
    fun <T : Any> tryCreateBeanModule(klass: KClass<T>, instance: T) {
        val scopeQualifier = KoinAnnotationProcessor.getScopeQualifier(klass)
        val qualifier = KoinAnnotationProcessor.getQualifier(klass)
        val bean = KoinAnnotationProcessor.getBeanProperties(klass) ?: return

        val module = when (bean.first) {
            Kind.Factory -> createFactoryBeanModule(klass, instance, scopeQualifier, qualifier, bean.second.toList())
            Kind.Singleton -> createSingletonBeanModule(klass, instance, scopeQualifier, qualifier, bean.second.toList())
            else -> { return }
        }
        getKoin().loadModules(listOf(module))
    }

    fun <T : Any> createSingletonBeanModule(
        klass: KClass<T>,
        instance: T,
        scopeQualifier: Qualifier = getKoin().scopeRegistry.rootScope.scopeQualifier,
        qualifier: Qualifier? = null,
        secondaryTypes: List<KClass<*>>,
        createdAtStart: Boolean = false
    ): Module {
        val beanDefinition = BeanDefinition(scopeQualifier, klass, qualifier, { instance }, Kind.Singleton, secondaryTypes)
        val singletonInstanceFactory = SingleInstanceFactory(beanDefinition)
        return Module(createdAtStart).apply {
            indexPrimaryType(singletonInstanceFactory)
            indexSecondaryTypes(singletonInstanceFactory)
        }
    }

    fun <T : Any> createFactoryBeanModule(
        klass: KClass<T>,
        instance: T,
        scopeQualifier: Qualifier = getKoin().scopeRegistry.rootScope.scopeQualifier,
        qualifier: Qualifier? = null,
        secondaryTypes: List<KClass<*>>,
        createdAtStart: Boolean = false
    ): Module {
        val beanDefinition = BeanDefinition(scopeQualifier, klass, qualifier, { instance }, Kind.Factory, secondaryTypes)
        val factoryInstanceFactory = FactoryInstanceFactory(beanDefinition)
        return Module(createdAtStart).apply {
            indexPrimaryType(factoryInstanceFactory)
            indexSecondaryTypes(factoryInstanceFactory)
        }
    }

    fun <T> Module.indexSecondaryTypes(factory: InstanceFactory<T>, allowOverride: Boolean = true) {
        factory.beanDefinition.secondaryTypes.forEach { secondClazz ->
            val mapping = indexKey(secondClazz, factory.beanDefinition.qualifier, factory.beanDefinition.scopeQualifier)
            saveMapping(mapping, factory, allowOverride)
        }
    }
}