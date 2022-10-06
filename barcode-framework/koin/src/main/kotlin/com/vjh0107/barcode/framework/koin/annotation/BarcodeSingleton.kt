package com.vjh0107.barcode.framework.koin.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class BarcodeSingleton(val binds: Array<KClass<*>> = [])
