package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.mmoitems

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeSkillMechanic
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import io.lumine.mythic.lib.api.player.EquipmentSlot
import io.lumine.mythic.lib.api.stat.modifier.ModifierSource
import io.lumine.mythic.lib.api.stat.modifier.ModifierType
import io.lumine.mythic.lib.api.stat.modifier.TemporaryStatModifier
import net.Indyuce.mmoitems.api.player.PlayerData
import org.bukkit.entity.Player
import java.util.*

@MythicMechanic(
    author = "vjh0107",
    name = "barcodestatbuff",
    aliases = ["bcsb"],
    description = "stat buff with duration"
)
class MMOItemsStatBuffMechanic(line: String, mlc: MythicLineConfig) : BarcodeSkillMechanic(line, mlc), INoTargetSkill {

    private val statName: PlaceholderString
    private val amount: PlaceholderDouble
    private val duration: PlaceholderDouble
    private val isRelative: PlaceholderString
    private val key: PlaceholderString
    private val isTargeted: Boolean


    init {
        this.isTargeted = mlc.getBoolean("isTargeted", false)
        this.statName = PlaceholderString.of(mlc.getString(arrayOf("stat", "s"), "1", *arrayOfNulls(0)))
        this.amount = PlaceholderDouble.of(mlc.getString(arrayOf("amount", "a"), "1", *arrayOfNulls(0)))
        this.duration = PlaceholderDouble.of(mlc.getString(arrayOf("duration", "d"), "1", *arrayOfNulls(0)))
        this.isRelative = PlaceholderString.of(mlc.getString(arrayOf("isRelative"), "false", *arrayOfNulls(0)))
        this.key = PlaceholderString.of(mlc.getString(arrayOf("key"), "random", *arrayOfNulls(0)))
    }

    override fun cast(data: SkillMetadata?): SkillResult {
        val targets = if (isTargeted) {
            data!!.entityTargets
                .filter { it.isPlayer }
                .map { it.bukkitEntity as Player }
                .filter { it.isOnline }
        } else {
            listOf(data!!.caster.entity.bukkitEntity as? Player ?: return SkillResult.CONDITION_FAILED)
        }

        targets.forEach { player ->
            val playerData = PlayerData.get(player)
            val playerStatInstance = playerData.stats.map.getInstance(statName.get(data).uppercase())
            val key = "BARCODE_BUFF_" + if (this.key.get(data) == "random")
                UUID.randomUUID().toString()
            else
                this.key.get(data)
            val temporaryModifier = TemporaryStatModifier(
                amount.get(data),
                duration.get(data).toLong(),
                if (isRelative.get(data) == "false") ModifierType.FLAT else ModifierType.RELATIVE,
                EquipmentSlot.ANY,
                ModifierSource.OTHER,
                key,
                playerStatInstance
            )
            playerData.stats.map.getInstance(statName.get(data)).addModifier(key, temporaryModifier)
        }
        return SkillResult.SUCCESS
    }
}