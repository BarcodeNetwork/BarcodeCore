package com.vjh0107.barcode.framework.component.handler.impl.registrable

/**
 * Registrable 안에서 사용해주세요.
 * Registrar 이 달린 메소드는, ComponentHandler 의 객체 생성과 동시에 실행됩니다.
 *
 * @param depend 플러그인이 존재하는지 검사 후 register 합니다.
 * @see BarcodeRegistrableManager
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Registrar(val depend: String = "null")
