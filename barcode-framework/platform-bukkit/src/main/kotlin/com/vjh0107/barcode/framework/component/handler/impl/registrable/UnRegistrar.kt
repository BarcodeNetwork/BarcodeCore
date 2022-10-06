package com.vjh0107.barcode.framework.component.handler.impl.registrable

/**
 * Registrable 안에서 사용해주세요.
 *
 * @param pluginName 플러그인이 존재하는지 검사 후 unregister 합니다.
 * @see BarcodeRegistrableManager
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class UnRegistrar(val pluginName: String = "null")
