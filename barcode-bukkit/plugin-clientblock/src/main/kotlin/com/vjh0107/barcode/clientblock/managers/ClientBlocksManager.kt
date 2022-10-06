package com.vjh0107.barcode.clientblock.managers

import com.vjh0107.barcode.clientblock.block.ClientBlocks
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodePluginManager
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider

@BarcodeComponent
class ClientBlocksManager : BarcodePluginManager {
    val clientBlocks: MutableMap<String, ClientBlocks> = mutableMapOf()

    @InjectInstance
    companion object : InstanceProvider<ClientBlocksManager> {
        override lateinit var instance: ClientBlocksManager
    }

    override fun load() {
        TODO("Not yet implemented")
    }

    fun load(name: String, clientBlocks: ClientBlocks) {
        clientBlocks.put(name, clientBlocks)
    }
}