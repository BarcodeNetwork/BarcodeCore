package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.mmoitems

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import net.Indyuce.mmoitems.ItemStats
import net.Indyuce.mmoitems.api.player.PlayerData
import org.bukkit.entity.Player

@MythicMechanic(author = "vjh0107", name = "setattackspeedcooldown", aliases = ["sasc"], description = "set attack speed cooldown")
class SetAttackSpeedCooldownMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc), INoTargetSkill {
    private var abilityRoute: PlaceholderString? = null
    init {
        this.abilityRoute = PlaceholderString.of(mlc.getString(arrayOf("ability", "a"), "1", *arrayOfNulls(0)))
    }

    override fun cast(data: SkillMetadata?): SkillResult {
        if (!data!!.caster.entity.isPlayer) return SkillResult.CONDITION_FAILED
        val caster = data.caster.entity.bukkitEntity as Player

        val playerData: PlayerData = PlayerData.get(caster)

        val stat = playerData.stats.getStat(ItemStats.ATTACK_SPEED)

        val attackSpeedToCooldown: Double = 1 / stat
        playerData.mmoPlayerData.cooldownMap.applyCooldown("mmoitems_skill_${abilityRoute.toString().lowercase()}", attackSpeedToCooldown)
        caster.setCooldown(caster.inventory.itemInMainHand.type, (attackSpeedToCooldown * 20).toInt())
        return SkillResult.SUCCESS

    }
}
