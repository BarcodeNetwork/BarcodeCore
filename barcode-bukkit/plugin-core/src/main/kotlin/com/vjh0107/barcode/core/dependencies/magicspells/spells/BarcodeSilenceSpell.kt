package com.vjh0107.barcode.core.dependencies.magicspells.spells

import com.nisovin.magicspells.spells.BuffSpell
import com.nisovin.magicspells.util.MagicConfig
import com.vjh0107.barcode.core.database.getCorePlayerData
import com.vjh0107.barcode.core.database.player.CorePlayerPlayerData
import com.vjh0107.barcode.framework.utils.transformer.isPlayer
import net.Indyuce.mmocore.MMOCore
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player


class BarcodeSilenceSpell(config: MagicConfig, spellName: String) : BuffSpell(config, spellName) {

    override fun castBuff(entity: LivingEntity, power: Float, args: Array<String?>?): Boolean {
        if(!entity.isPlayer()) return false
        val player: Player = entity.player ?: return false
        val playerDAO: CorePlayerPlayerData = player.getCorePlayerData()
        player.sendTitle("", MMOCore.plugin.configManager.getSimpleMessage("vjh0107.silenced", *arrayOfNulls(0)).message(), 0, duration.toInt()*20, 0)
        playerDAO.isSilenced = true
        return true
    }

    override fun isActive(entity: LivingEntity?): Boolean {
        val player: Player = entity as? Player ?: return false
        val playerDAO: CorePlayerPlayerData = player.getCorePlayerData()
        return playerDAO.isSilenced
    }

    override fun turnOffBuff(entity: LivingEntity) {
        if(!entity.isPlayer()) return
        val player: Player = entity.player ?: return
        val playerDAO: CorePlayerPlayerData = player.getCorePlayerData()
        playerDAO.isSilenced = false

    }

    override fun turnOff() {
    }

}