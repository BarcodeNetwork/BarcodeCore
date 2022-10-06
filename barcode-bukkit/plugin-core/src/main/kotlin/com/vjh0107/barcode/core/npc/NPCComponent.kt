package com.vjh0107.barcode.core.npc

import com.vjh0107.barcode.core.npc.datastore.TemporaryNPCDataStore
import com.vjh0107.barcode.core.npc.traits.BarcodeAttackTrait
import com.vjh0107.barcode.framework.AbstractBarcodePlugin
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeRegistrable
import com.vjh0107.barcode.framework.component.handler.impl.registrable.Registrar
import com.vjh0107.barcode.framework.component.handler.impl.registrable.UnRegistrar
import com.vjh0107.barcode.framework.injection.instance.InjectInstance
import com.vjh0107.barcode.framework.injection.instance.InstanceProvider
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPCRegistry
import net.citizensnpcs.api.trait.Trait
import net.citizensnpcs.api.trait.TraitInfo

@BarcodeComponent
class NPCComponent(val plugin: AbstractBarcodePlugin) : NPCAdapter, BarcodeRegistrable {
    override val id: String = "NPCComponent"

    private val temporaryNpcDataStore = TemporaryNPCDataStore.of(plugin)
    private val registeredTraits = mutableListOf<TraitInfo>()

    @InjectInstance
    companion object : InstanceProvider<NPCComponent> {
        override lateinit var instance: NPCComponent
    }

    override fun getNPCRegistry() : NPCRegistry {
        return CitizensAPI.getNamedNPCRegistry(temporaryNpcDataStore.name)
    }

    @Registrar("Citizens")
    fun registerNPCRegistry() {
        CitizensAPI.createNamedNPCRegistry(temporaryNpcDataStore.name, temporaryNpcDataStore)
    }

    @UnRegistrar("Citizens")
    fun unregisterNPCRegistry() {
        CitizensAPI.removeNamedNPCRegistry(temporaryNpcDataStore.name)
    }

    @Registrar("Sentinel")
    fun registerTraits() {
        registerTrait(BarcodeAttackTrait::class.java, "barcodeattack")
    }

    @UnRegistrar("Sentinel")
    fun unregisterTraits() {
        registeredTraits.forEach {
            CitizensAPI.getTraitFactory().deregisterTrait(it)
        }
    }

    private fun <T : Trait> registerTrait(trait: Class<T>, withName: String) {
        val traitInfo = TraitInfo.create(trait).withName(withName)

        registeredTraits.add(traitInfo)
        CitizensAPI.getTraitFactory().registerTrait(traitInfo)
    }
}