package com.vjh0107.barcode.framework.reflection.injection.models

import com.vjh0107.barcode.framework.component.IBarcodeComponent
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider

class ExampleComponent : IBarcodeComponent {
    @InjectInstance
    companion object : InstanceProvider<ExampleComponent> {
        override lateinit var instance: ExampleComponent
    }

    fun printAA() {
        println("AA")
    }

    fun getResult(): Boolean {
        return true
    }
}