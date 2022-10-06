package com.vjh0107.barcode.framework.koin.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class BarcodeFactory(val binds: Array<KClass<*>> = [])