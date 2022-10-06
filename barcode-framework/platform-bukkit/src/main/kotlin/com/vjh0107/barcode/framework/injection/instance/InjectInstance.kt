package com.vjh0107.barcode.framework.injection.instance

/**
 * InstanceProvider<T> 가 구현된 companion object 의 instance 필드를
 * 객체의 생성 시(ComponentHandler 와 AbstractBarcodePlugin 등을 통한) 주입시켜줍니다.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class InjectInstance
