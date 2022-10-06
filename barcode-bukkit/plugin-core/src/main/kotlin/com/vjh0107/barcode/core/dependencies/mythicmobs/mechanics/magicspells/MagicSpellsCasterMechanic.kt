package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.magicspells

import com.nisovin.magicspells.MagicSpells
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import org.bukkit.entity.LivingEntity
import java.security.InvalidParameterException

@MythicMechanic(author = "vjh0107", name = "castmagicspells", aliases = ["cms"], description = "Cast MagicSpells")
class MagicSpellsCasterMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc), INoTargetSkill {
    private var spellName: PlaceholderString? = null
    init {
        this.spellName = PlaceholderString.of(mlc.getString(arrayOf("spell", "s"), "1", *arrayOfNulls(0)))
    }

    override fun cast(data: SkillMetadata?): SkillResult {
        val target = data!!.caster.entity
        return if (!(target.isDead || target.health <= 0)) {

            val livingEntity: LivingEntity = data.caster.entity.bukkitEntity as LivingEntity
            MagicSpells.getSpellByInGameName(this.spellName!!.get(data))?.cast(livingEntity)
                ?: throw InvalidParameterException("Cant find spell called: $spellName")

            SkillResult.SUCCESS
        } else {
            SkillResult.CONDITION_FAILED
        }
    }
}