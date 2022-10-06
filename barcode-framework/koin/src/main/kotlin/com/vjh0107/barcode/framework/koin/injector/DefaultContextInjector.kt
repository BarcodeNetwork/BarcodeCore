package com.vjh0107.barcode.framework.koin.injector

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools

/**
 * KoinComponent 없이 DefaultContext 를 통해 inject 에 필요한 파라미터를 가져옵니다.
 * @param qualifier
 * @param parameters
 */
inline fun <reified T : Any> get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    return KoinPlatformTools.defaultContext().get().get(qualifier, parameters)
}

/**
 * KoinComponent 없이 DefaultContext 를 통해 inject 합니다.
 * @param qualifier
 * @param mode - LazyThreadSafetyMode
 * @param parameters
 */
inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = lazy(mode) { get<T>(qualifier, parameters) }