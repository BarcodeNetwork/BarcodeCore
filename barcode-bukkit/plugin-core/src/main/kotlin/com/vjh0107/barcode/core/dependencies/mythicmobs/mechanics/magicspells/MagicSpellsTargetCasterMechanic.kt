package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.magicspells

import com.nisovin.magicspells.MagicSpells
import com.nisovin.magicspells.spells.TargetedEntitySpell
import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import org.bukkit.entity.LivingEntity

@MythicMechanic(author = "vjh0107", name = "casttargetedmagicspells", aliases = ["ctms"], description = "Cast targeted MagicSpells")
class MagicSpellsTargetCasterMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc),
    ITargetedEntitySkill {
    private var spellName: PlaceholderString? = null
    init {
        this.spellName = PlaceholderString.of(mlc.getString(arrayOf("spell", "s"), "1", *arrayOfNulls(0)))
    }

    override fun castAtEntity(data: SkillMetadata?, targetEntity: AbstractEntity?): SkillResult {
        val caster = data!!.caster.entity
        val target = targetEntity ?: return SkillResult.CONDITION_FAILED
        val casterL: LivingEntity = caster.bukkitEntity as LivingEntity
        val targetL: LivingEntity = target.bukkitEntity as LivingEntity? ?: return SkillResult.CONDITION_FAILED
        return if (!(caster.isDead || caster.health <= 0)) {
            try {
                val spell = MagicSpells.getSpellByInGameName(this.spellName!!.get(data)) as TargetedEntitySpell
                spell.castAtEntity(casterL, targetL, 1F)
            } catch (event: ClassCastException) {
                throw ClassCastException("spell is not TargetedEntitySpell")
            }
            SkillResult.SUCCESS
        } else {
            SkillResult.CONDITION_FAILED
        }
    }
}