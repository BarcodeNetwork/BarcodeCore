package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.skills

import com.vjh0107.barcode.core.database.getCorePlayerData
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
import java.util.*

@MythicMechanic(author = "vjh0107", name = "buffindicator", aliases = ["bi"], description = "buff indicator custom actionbars integration")
class BuffIndicatorMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc), INoTargetSkill {
    private val skillIcon: PlaceholderString
    private val amount: PlaceholderDouble
    private val isCooldownAbilityApplied: Boolean
    private val isDebuff: Boolean
    private val key: PlaceholderString
    init {
        this.skillIcon = PlaceholderString.of(mlc.getString(arrayOf("skillIcon", "icon"), "\ue1e4", *arrayOfNulls(0)))
        this.key = PlaceholderString.of(mlc.getString(arrayOf("key", "k"), "not set", *arrayOfNulls(0)))
        this.amount = PlaceholderDouble.of(mlc.getString(arrayOf("amount", "a"), "1", *arrayOfNulls(0)))
        this.isCooldownAbilityApplied = mlc.getBoolean(arrayOf("isCooldownAbilityApplied"), false)
        this.isDebuff = mlc.getBoolean(arrayOf("isDebuff"), false)
    }

    override fun cast(data: SkillMetadata?): SkillResult {
        if (!data!!.caster.entity.isPlayer) return SkillResult.CONDITION_FAILED
        val player = data.caster.entity.bukkitEntity as? Player ?: return SkillResult.CONDITION_FAILED
        val cooldown: Double = if (isCooldownAbilityApplied) {
            val cooldownReduction = PlayerData.get(player).stats.map.getStat("COOLDOWN_REDUCTION")
            this.amount.get(data) - (this.amount.get(data) * cooldownReduction/100.0)

        } else {
            this.amount.get(data)
        }
        val buffKey = if (this.key.get(data) == "not set") {
            UUID.randomUUID().toString()
        } else {
            this.key.get(data)
        }
        if (amount.get(data) == 0.0)
            player.getCorePlayerData().buffIndicator.removeBuffIndicatorCooldown(buffKey)
        else
            player.getCorePlayerData().buffIndicator.setBuffIndicatorCooldown(this.skillIcon.get(data), cooldown, this.isDebuff, buffKey)

        return SkillResult.SUCCESS
    }
}