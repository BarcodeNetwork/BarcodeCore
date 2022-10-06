package com.vjh0107.barcode.core.skill.defaultattack

import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter
import com.vjh0107.barcode.framework.component.BarcodeComponent
import com.vjh0107.barcode.framework.component.BarcodeListener
import io.lumine.mythic.lib.MythicLib
import net.Indyuce.mmoitems.api.interaction.UseItem
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerAnimationEvent

@BarcodeComponent
class PlayerLeftDefaultAttackListener : BarcodeListener {
    @EventHandler
    fun onLeftClick(event: PlayerAnimationEvent) {
        val handItem = event.player.inventory.itemInMainHand
        if (handItem.type.isAir) return
        if (event.player.hasCooldown(handItem.type)) return

        val item = MythicLib.plugin.version.wrapper.getNBTItem(handItem)
        if (!item.hasType()) return

        val player = event.player
        val useItem = UseItem.getItem(player, item, item.type)

        if (!useItem.checkItemRequirements()) {
            event.isCancelled = true
            return
        }

        val barcodeAbility: String = item.getString("MMOITEMS_BARCODE_DEFAULT_ATTACK_ABILITY") ?: return
        MythicMobsAdapter.inst().apiHelper.castSkill(player, barcodeAbility)
    }
}