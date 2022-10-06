package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.skills

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit

@MythicMechanic(author = "vjh0107", name = "fontskill", aliases = ["FontSkill"], description = "parse placeholder string and set name of activemobs")
class FontSkillMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc), INoTargetSkill {
    private val fontString: PlaceholderString
    init {
        val fontStr = mlc.getString(arrayOf("font", "f"), "NOT SET", *arrayOfNulls(0))
        this.fontString = PlaceholderString.of(fontStr)
    }

    override fun cast(data: SkillMetadata?): SkillResult {
        val target = Bukkit.getServer().onlinePlayers.first()
        return if (!(target.isDead)) {
            val parsedString = PlaceholderAPI.setPlaceholders(target, this.fontString.get(data))
            data!!.caster.entity.bukkitEntity.customName = parsedString
            SkillResult.SUCCESS
        } else {
            SkillResult.CONDITION_FAILED
        }
    }
}