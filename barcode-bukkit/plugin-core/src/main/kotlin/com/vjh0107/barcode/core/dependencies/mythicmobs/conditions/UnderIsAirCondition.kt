package com.vjh0107.barcode.core.dependencies.mythicmobs.conditions

import com.vjh0107.barcode.framework.utils.underIsAir
import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.conditions.IEntityCondition
import io.lumine.mythic.core.skills.SkillCondition
import io.lumine.mythic.core.utils.annotations.MythicCondition
import io.lumine.mythic.core.utils.annotations.MythicField
import org.bukkit.entity.LivingEntity

@MythicCondition(
    author = "vjh0107",
    name = "underisair",
    aliases = [],
    description = "check block count of caster and ground"
)
class UnderIsAirCondition(line: String?, mlc: MythicLineConfig) : SkillCondition(line), IEntityCondition {

    @MythicField(name = "blockcount", aliases = ["b"], description = "The block count to match")
    private var blockCount: Int

    init {
        blockCount = mlc.getInteger(arrayOf("distance", "d"), 1)
    }

    override fun check(p0: AbstractEntity?): Boolean {
        p0 ?: return false
        val livingEntity = p0 as? LivingEntity ?: return false
        return livingEntity.underIsAir(blockCount)
    }
}
