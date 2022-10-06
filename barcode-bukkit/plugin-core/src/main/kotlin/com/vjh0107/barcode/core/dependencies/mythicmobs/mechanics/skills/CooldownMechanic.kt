package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.skills

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import net.Indyuce.mmoitems.api.player.PlayerData
import org.bukkit.Material
import org.bukkit.entity.Player

@MythicMechanic(author = "vjh0107", name = "cooldownsetter", aliases = ["cooldownsetter"], description = "cooldown setter using bukkitapi")
class CooldownMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc), INoTargetSkill {
    private val amount: PlaceholderDouble
    private val isHand: Boolean
    private val material: Material
    private val isCooldownAbilityApplied: Boolean

    init {
        val strAmount = mlc.getString(arrayOf("amount", "a"), "1", *arrayOfNulls(0))
        amount = PlaceholderDouble.of(strAmount)
        this.isHand = mlc.getBoolean(arrayOf("isHand", "iH"), false)
        this.material = Material.getMaterial(mlc.getString(arrayOf("material", "m"), "stone").uppercase()) ?: Material.STONE
        this.isCooldownAbilityApplied = mlc.getBoolean(arrayOf("isCooldownAbilityApplied"), false)
    }

    override fun cast(data: SkillMetadata?): SkillResult {
        val player: Player = data!!.caster.entity.bukkitEntity as? Player ?: return SkillResult.CONDITION_FAILED
        if (player.isDead || player.health <= 0) return SkillResult.CONDITION_FAILED
        val cooldown: Int = if (isCooldownAbilityApplied) {
            val mmoItemsPlayerData: PlayerData = PlayerData.get(player)
            val cooldownReduction = mmoItemsPlayerData.stats.map.getStat("COOLDOWN_REDUCTION")
            (this.amount.get(data) - (this.amount.get(data) * cooldownReduction/100.0)).toInt()
        } else {
            this.amount.get(data).toInt()
        }
        return if (isHand) {
            val playerHand: Material = player.inventory.itemInMainHand.type
            player.setCooldown(playerHand, cooldown)
            SkillResult.SUCCESS
        } else {
            player.setCooldown(material, cooldown)
            SkillResult.SUCCESS
        }
    }
}