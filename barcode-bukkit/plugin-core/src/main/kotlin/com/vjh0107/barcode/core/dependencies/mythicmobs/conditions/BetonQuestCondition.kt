package com.vjh0107.barcode.core.dependencies.mythicmobs.conditions

import com.vjh0107.barcode.framework.utils.transformer.isPlayer
import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.conditions.IEntityCondition
import io.lumine.mythic.core.skills.SkillCondition
import io.lumine.mythic.core.utils.annotations.MythicCondition
import io.lumine.mythic.core.utils.annotations.MythicField
import org.betonquest.betonquest.BetonQuest
import org.betonquest.betonquest.id.ConditionID
import org.betonquest.betonquest.utils.PlayerConverter
import org.bukkit.entity.LivingEntity

@MythicCondition(
    author = "vjh0107",
    name = "bqc",
    aliases = ["BQcon", "BetonQuestCondition"],
    description = "check beton quest condition of player"
)
class BetonQuestCondition(line: String?, mlc: MythicLineConfig) :
    SkillCondition(line), IEntityCondition {

    @MythicField(name = "conditionName", aliases = ["condition", "name", "cn"], description = "bq condition package name")
    private var bqCondition: String

    init {
        bqCondition = mlc.getString(arrayOf("condition", "name", "cn"))
    }

    override fun check(p0: AbstractEntity?): Boolean {
        p0 ?: return false
        val maybePlayer = p0 as? LivingEntity ?: return false
        if (!maybePlayer.isPlayer()) return false
        return BetonQuest.condition(PlayerConverter.getID(maybePlayer), ConditionID(null, bqCondition))
    }
}