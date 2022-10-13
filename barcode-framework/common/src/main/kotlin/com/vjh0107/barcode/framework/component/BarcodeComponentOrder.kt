package com.vjh0107.barcode.framework.component

/**
 * BarcodeComponent 의 우선순위를 정합니다.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class BarcodeComponentOrder(val order: Int = defaultComponentOrder)
