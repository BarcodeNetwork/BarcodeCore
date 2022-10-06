package com.vjh0107.barcode.core.gps

import com.live.bemmamin.gps.api.GPSAPI
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider

@BarcodeComponent
class GPSComponent(plugin: AbstractBarcodePlugin) : BarcodeRegistrable {
    override val id: String = "GPSComponent"

    val gpsAPI = GPSAPI(plugin)

    @InjectInstance
    companion object : InstanceProvider<GPSComponent> {
        override lateinit var instance: GPSComponent
    }
}