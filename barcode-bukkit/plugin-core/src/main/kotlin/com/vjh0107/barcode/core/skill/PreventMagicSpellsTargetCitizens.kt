package com.vjh0107.barcode.core.skill

import com.nisovin.magicspells.events.SpellTargetEvent
import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter.toAbstractEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PreventMagicSpellsTargetCitizens : Listener {
    @EventHandler
    fun onTarget(event: SpellTargetEvent) {
        if (event.target.toAbstractEntity().isCitizensNPC) {
            event.isCancelled = true
        }
    }
}