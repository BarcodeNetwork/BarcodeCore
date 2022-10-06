package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.mmoitems

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import net.Indyuce.mmocore.api.player.PlayerData
import org.bukkit.entity.Player

@MythicMechanic(author = "vjh0107", name = "addMana", aliases = ["addMana"], description = "addMana")
class ManaMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc), INoTargetSkill {
    private val amount: PlaceholderDouble
    init {
        val strAmount = mlc.getString(arrayOf("s", "amount", "a"), "1", *arrayOfNulls(0))
        amount = PlaceholderDouble.of(strAmount)
        //this.amount = PlaceholderDouble.of(mlc.getString(arrayOf("a", "amount", "s"), "1", *arrayOfNulls(0)))
    }

    override fun cast(data: SkillMetadata?): SkillResult {
        val target: Player = data!!.caster.entity.bukkitEntity as? Player ?: return SkillResult.CONDITION_FAILED
        return if (!(target.isDead || target.health <= 0)) {
            val mmoCorePlayerData: PlayerData = PlayerData.get(target)
            mmoCorePlayerData.giveMana(amount.get(data))
            SkillResult.SUCCESS
        } else {
            SkillResult.CONDITION_FAILED
        }
    }
}