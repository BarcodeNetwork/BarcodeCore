package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.mmoitems

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import net.Indyuce.mmoitems.api.player.PlayerData
import org.bukkit.entity.Player

@MythicMechanic(author = "vjh0107", name = "setmiabilitycooldown", aliases = ["smac"], description = "setmmoitemsabilitycooldown")
class SetMMOItemsCooldownMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc), INoTargetSkill {
    private val abilityRoute: PlaceholderString
    private val amount: PlaceholderDouble
    private val isCooldownAbilityApplied: Boolean
    init {
        this.abilityRoute = PlaceholderString.of(mlc.getString(arrayOf("ability", "a"), "1", *arrayOfNulls(0)))
        val strAmount = mlc.getString(arrayOf("amount", "a"), "1", *arrayOfNulls(0))
        amount = PlaceholderDouble.of(strAmount)
        this.isCooldownAbilityApplied = mlc.getBoolean(arrayOf("isCooldownAbilityApplied"), false)
    }

    override fun cast(data: SkillMetadata?): SkillResult {
        if (!data!!.caster.entity.isPlayer) return SkillResult.CONDITION_FAILED
        val caster = data.caster.entity.bukkitEntity as Player

        val playerData: PlayerData = PlayerData.get(caster)

        val cooldown: Double = if (isCooldownAbilityApplied) {
            val mmoItemsPlayerData: PlayerData = PlayerData.get(caster)
            val cooldownReduction = mmoItemsPlayerData.stats.map.getStat("COOLDOWN_REDUCTION")
            (this.amount.get(data) - (this.amount.get(data) * cooldownReduction/100.0))
        } else {
            this.amount.get(data)
        }
        playerData.mmoPlayerData.cooldownMap.applyCooldown("mmoitems_skill_${abilityRoute.toString().lowercase()}", cooldown)

        return SkillResult.SUCCESS

    }
}
