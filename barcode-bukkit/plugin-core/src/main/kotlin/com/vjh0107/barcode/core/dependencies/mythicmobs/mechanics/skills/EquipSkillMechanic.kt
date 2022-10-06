package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.skills

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import com.vjh0107.barcode.framework.utils.item.createGuiItem
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import org.bukkit.Material
import org.bukkit.entity.LivingEntity

@MythicMechanic(author = "vjh0107", name = "equipskill", aliases = ["equipSkill"], description = "equip barcode skill resources to actiemobs")
class EquipSkillMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc), INoTargetSkill {
    private val item: PlaceholderString
    private val amount: PlaceholderInt
    init {
        val strAmount = mlc.getString(arrayOf("custommodeldata", "c"), "1", *arrayOfNulls(0))
        val itemString = mlc.getString(arrayOf("item", "i"), "SLIME_BALL", *arrayOfNulls(0))
        amount = PlaceholderInt.of(strAmount)
        item = PlaceholderString.of(itemString)
    }

    override fun cast(data: SkillMetadata?): SkillResult {
        val target = data!!.caster.entity.bukkitEntity as LivingEntity
        return if (!(target.isDead)) {
                val item = createGuiItem(Material.valueOf(item.get(data)), customModelData = amount.get(data))
                target.equipment?.setHelmet(item) ?: throw NullPointerException("can't get equipments of livingentity")
            SkillResult.SUCCESS
        } else {
            SkillResult.CONDITION_FAILED
        }
    }
}