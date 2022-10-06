package com.vjh0107.barcode.framework.injection.instance.injector

interface InstanceInjector<T> {
    fun inject(target: T)
}