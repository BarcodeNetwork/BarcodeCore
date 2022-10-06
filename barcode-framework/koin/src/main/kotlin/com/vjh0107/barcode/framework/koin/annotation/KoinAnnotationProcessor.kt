package com.vjh0107.barcode.framework.koin.annotation

import com.vjh0107.barcode.framework.koin.getKoin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.annotation.Named
import org.koin.core.annotation.Scope
import org.koin.core.definition.Kind
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.TypeQualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@OptIn(KoinInternalApi::class)
object KoinAnnotationProcessor {
    /**
     * 스코프 Qualifier 를 구합니다.
     */
    fun getScopeQualifier(element: KAnnotatedElement): Qualifier {
        val scopeAnnotation = element.findAnnotation<Scope>()
        return if (scopeAnnotation?.value != null) {
            TypeQualifier(scopeAnnotation.value)
        } else if (scopeAnnotation?.name != null) {
            StringQualifier(scopeAnnotation.name)
        } else {
            getKoin().scopeRegistry.rootScope.scopeQualifier
        }
    }

    /**
     * Qualifier 를 구합니다.
     */
    fun getQualifier(element: KAnnotatedElement): Qualifier? {
        return element.findAnnotation<Named>()?.value?.let { StringQualifier(it) }
    }

    /**
     * 빈의 종류와 Bind 타입들을 구합니다.
     */
    fun getBeanProperties(klass: KClass<*>): Pair<Kind, Array<KClass<*>>>? {
        val factory = klass.findAnnotation<BarcodeFactory>()
        val singleton = klass.findAnnotation<BarcodeSingleton>()

        if (factory != null && singleton != null) {
            throw IllegalArgumentException("BarcodeFactory 와 BarcodeSingleton 은 공존할 수 없습니다.")
        }

        return when(true) {
            (factory != null) -> Pair(Kind.Factory, factory.binds)
            (singleton != null) -> Pair(Kind.Singleton, singleton.binds)
            else -> null
        }
    }
}