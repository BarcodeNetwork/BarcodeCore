package com.vjh0107.barcode.core.dependencies.mythicmobs.drops

import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter.toBukkitLocation
import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter.toBukkitPlayer
import io.lumine.mythic.api.adapters.AbstractPlayer
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.drops.DropMetadata
import io.lumine.mythic.api.drops.IIntangibleDrop
import io.lumine.mythic.core.drops.Drop
import net.Indyuce.mmocore.MMOCore
import net.Indyuce.mmocore.api.player.PlayerData
import net.Indyuce.mmocore.experience.EXPSource
import net.Indyuce.mmocore.experience.Profession

class MMOCoreMeisterExpDrop(config: MythicLineConfig) : Drop(config.line, config), IIntangibleDrop {

    private val professionId: String = config.getString("professionId")
        .replace(" ", "-")
        .replace("_", "-")
        .lowercase()
    private val xpAmount: Double = config.getDouble("amount", 0.0)

    override fun giveDrop(target: AbstractPlayer, metadata: DropMetadata, var3: Double) {
        val player = target.toBukkitPlayer()
        if (player.isOnline.not()) {
            return
        }
        val mmoCorePlayerData: PlayerData = PlayerData.get(player)
        val dropperLoc = metadata.dropper.get().location.toBukkitLocation()
        val profession: Profession = MMOCore.plugin.professionManager.get(professionId) ?: throw NullPointerException()
        mmoCorePlayerData.collectionSkills.giveExperience(profession, xpAmount, EXPSource.OTHER, dropperLoc)
    }
}
