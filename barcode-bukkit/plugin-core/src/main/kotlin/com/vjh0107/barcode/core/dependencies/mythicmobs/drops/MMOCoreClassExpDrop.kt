package com.vjh0107.barcode.core.dependencies.mythicmobs.drops

import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter.toBukkitLocation
import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter.toBukkitPlayer
import com.vjh0107.barcode.framework.utils.messagesender.sendBNWarnMessage
import io.lumine.mythic.api.adapters.AbstractPlayer
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.drops.DropMetadata
import io.lumine.mythic.api.drops.IIntangibleDrop
import io.lumine.mythic.core.drops.Drop
import net.Indyuce.mmocore.api.player.PlayerData
import net.Indyuce.mmocore.experience.EXPSource

class MMOCoreClassExpDrop(config: MythicLineConfig) : Drop(config.line, config), IIntangibleDrop {
    private val xpAmount: Double = config.getDouble("amount", 0.0)

    private val xpRestrictionLevelBase: Int = config.getInteger("level", 0)
    private val xpRestrictionLevelRange: Int = config.getInteger("range", 10)
    private val xpRestrictionLevelParameter: Double = config.getDouble("parameter", 0.3)

    override fun giveDrop(target: AbstractPlayer, metadata: DropMetadata, amount: Double) {
        val player = target.toBukkitPlayer()
        if (player.isOnline.not()) {
            return
        }
        val mmoCorePlayerData: PlayerData = PlayerData.get(player)
        val dropperLoc = metadata.dropper.get().location.toBukkitLocation()

        val resultXp = if (xpRestrictionLevelBase == 0) {
            xpAmount
        } else {
            if (xpRestrictionLevelBase - mmoCorePlayerData.level > xpRestrictionLevelRange) {
                player.sendBNWarnMessage("몬스터와의 레벨 차이가 너무 높아, 획득 경험치가 경감됩니다.")
                xpAmount * xpRestrictionLevelParameter
            } else {
                xpAmount
            }
        }
        mmoCorePlayerData.giveExperience(resultXp, EXPSource.OTHER, dropperLoc)
    }
}
