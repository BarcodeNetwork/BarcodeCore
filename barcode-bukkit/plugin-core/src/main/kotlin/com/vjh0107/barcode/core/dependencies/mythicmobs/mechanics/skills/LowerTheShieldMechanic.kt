package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.skills

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@MythicMechanic(author = "vjh0107", name = "lowertheshield", aliases = ["lowerTheShield"], description = "lower the player's shield")
class LowerTheShieldMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc), INoTargetSkill {
    override fun cast(data: SkillMetadata?): SkillResult {
        val player: Player = data!!.caster.entity.bukkitEntity as? Player ?: return SkillResult.CONDITION_FAILED
        return if (!(player.isDead || player.health <= 0)) {
            player.openInventory(Bukkit.createInventory(null, 9, "Parring"))
            player.closeInventory()
            SkillResult.SUCCESS
        } else {
            SkillResult.CONDITION_FAILED
        }
    }
}