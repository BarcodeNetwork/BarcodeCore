package com.vjh0107.barcode.core.npc.datastore

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.npc.NPCDataStore
import net.citizensnpcs.api.npc.NPCRegistry

interface TemporaryNPCDataStore : NPCDataStore {
    /**
     * NPCDataStore 이름입니다. register 과 unregister (deregister) 될 때 사용됩니다.
     */
    val name: String

    // do not implement {
    override fun clearData(p0: NPC?) {}
    override fun loadInto(p0: NPCRegistry?) {}
    override fun saveToDisk() {}
    override fun saveToDiskImmediate() {}
    override fun store(p0: NPC?) {}
    override fun storeAll(p0: NPCRegistry?) {}
    override fun reloadFromSource() {}
    // }

    companion object {
        fun of(plugin: AbstractBarcodePlugin) : TemporaryNPCDataStore {
            return TemporaryNPCDataStoreImpl(plugin)
        }
    }
}