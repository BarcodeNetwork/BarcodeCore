package com.vjh0107.barcode.framework.component.handler

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class BarcodeComponentHandler(val priority: HandlerPriority = HandlerPriority.DEFAULT)
