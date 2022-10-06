package com.vjh0107.barcode.core.npc.datastore

import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import net.citizensnpcs.api.npc.NPCRegistry

class TemporaryNPCDataStoreImpl(plugin: AbstractBarcodePlugin) : TemporaryNPCDataStore {
    override val name = "TemporaryNPCDataStore"
    private var npcIDStarts: Int

    init {
        npcIDStarts = plugin.config.getInt("npc.temporary-npc-id-starts", 100000)
    }

    override fun createUniqueNPCId(registry: NPCRegistry): Int {
        npcIDStarts += 1
        if (registry.getById(npcIDStarts) != null) {
            throw RuntimeException("NPC ID $npcIDStarts 가 이미 있어 Temporary NPC 를 생성할 수 없습니다.")
        }
        return npcIDStarts
    }
}