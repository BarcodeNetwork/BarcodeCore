package com.vjh0107.barcode.core.npc.listeners

import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAPIAdapter
import com.vjh0107.barcode.core.npc.traits.BarcodeAttackTrait
import com.vjh0107.barcode.core.npc.utils.isNPC
import com.vjh0107.barcode.core.skill.isCalledByAllowedDamageSources
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

@BarcodeComponent
class SentinelListener : BarcodeListener {

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        if (!event.damager.isNPC()) return
        val npc = CitizensAPI.getNPCRegistry().getNPC(event.damager) ?: return
        if (npc.hasTrait(BarcodeAttackTrait::class.java) && !isCalledByAllowedDamageSources()) {
            onBarcodeAttackerSentinelDamage(event, npc)
        }
    }

    private fun onBarcodeAttackerSentinelDamage(event: EntityDamageByEntityEvent, npc: NPC) {
        event.isCancelled = true
        val attackTrait = npc.getTraitNullable(BarcodeAttackTrait::class.java) ?: return
        MythicMobsAPIAdapter.castSkill(npc.entity, attackTrait.attackSkillID)
    }
}