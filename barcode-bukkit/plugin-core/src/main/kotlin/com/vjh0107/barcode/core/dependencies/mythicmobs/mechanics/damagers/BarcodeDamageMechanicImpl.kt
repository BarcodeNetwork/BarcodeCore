package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.damagers

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeDamageMechanic
import com.vjh0107.barcode.core.skill.damager.getSkillDamageModifier
import com.vjh0107.barcode.core.skill.damager.giveBarcodeDamage
import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import io.lumine.mythic.lib.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

@MythicMechanic(
    author = "vjh0107",
    name = "barcodedamage",
    aliases = ["barcodedamage"],
    description = "barcode damage mechanic"
)
class BarcodeDamageMechanicImpl(line: String, mlc: MythicLineConfig) : BarcodeDamageMechanic(line, mlc), ITargetedEntitySkill {
    private val skillNameHolder: PlaceholderString
    private val damageKeyHolder: PlaceholderString
    private val damageTypesHolder: PlaceholderString

    init {
        val skillNameGet =
            mlc.getString(arrayOf("skillName", "name"), "NOT SET", *arrayOfNulls(0))
        this.skillNameHolder = PlaceholderString.of(skillNameGet)
        val damageKeyGet =
            mlc.getString(arrayOf("damageKey", "key"), "NOT SET", *arrayOfNulls(0))
        this.damageKeyHolder = PlaceholderString.of(damageKeyGet)
        val damageTypesHolderGet =
            mlc.getString(arrayOf("damageTypes", "types"), "NOT SET", *arrayOfNulls(0))
        this.damageTypesHolder = PlaceholderString.of(damageTypesHolderGet)
    }

    override fun castAtEntity(data: SkillMetadata?, target: AbstractEntity): SkillResult {
        if (!data!!.caster.entity.isPlayer) return SkillResult.CONDITION_FAILED
        val caster = data.caster.entity.bukkitEntity as? Player ?: return SkillResult.CONDITION_FAILED
        if (caster.isDead) return SkillResult.CONDITION_FAILED
        val damageTypes = parseDamageTypes(damageTypesHolder.get(data))
        val skillName = skillNameHolder.get(data)
        val damageKey = damageKeyHolder.get(data)
        val livingEntityTarget: LivingEntity = target.bukkitEntity as? LivingEntity ?: return SkillResult.CONDITION_FAILED
        livingEntityTarget.giveBarcodeDamage(caster, caster.getSkillDamageModifier(skillName, damageKey), damageTypes)
        return SkillResult.SUCCESS
    }

    private fun parseDamageTypes(rawString: String): Array<DamageType> {
        val damageTypes: MutableList<DamageType> = mutableListOf<DamageType>()

        val damageTypesSplited = rawString.uppercase().replace(" ", "").split(",")
        damageTypesSplited.forEach { singleDamageType ->
            damageTypes.add(DamageType.valueOf(singleDamageType))
        }

        return damageTypes.map { it }.toTypedArray()
    }
}