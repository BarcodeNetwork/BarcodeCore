package com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.damagers

import com.vjh0107.barcode.core.dependencies.mythicmobs.mechanics.BarcodeDamageMechanic
import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble
import io.lumine.mythic.core.mobs.ActiveMob
import io.lumine.mythic.core.utils.annotations.MythicMechanic
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

@MythicMechanic(author = "vjh0107", name = "damagebyowner", aliases = ["dbo"], description = "damaging mechanic that attacker is owner")
class DamageByOwnerMechanic(line: String, mlc: MythicLineConfig) : BarcodeDamageMechanic(line, mlc),
    ITargetedEntitySkill {
    private var amount: PlaceholderDouble? = null
    init {
        this.amount = PlaceholderDouble.of(mlc.getString(arrayOf("amount", "a"), "1", *arrayOfNulls(0)))
    }
    override fun castAtEntity(data: SkillMetadata, target: AbstractEntity): SkillResult {
        if (!target.isDead && !data.caster.isUsingDamageSkill && (!target.isLiving || !(target.health <= 0))) {
            val damage: Double = this.amount!!.get(data, target) * data.power.toDouble()
            if (data.caster.entity.isPlayer) {
                Bukkit.getServer().broadcastMessage("DamageByOwner 메카닉은 플레이어가 사용할 수 없습니다. (owner가 없기 때문입니다)")
                return SkillResult.ERROR
            }
            val caster = data.caster as ActiveMob

            var owner: Player? = null
            caster.owner.ifPresent {
                owner = Bukkit.getPlayer(it)
            }
            if (owner == null) {
                Bukkit.getServer().broadcastMessage("DamageByOwner 메카닉에서 지정된 ActiveMob의 owner가 null입니다 이는 정상적인 상황이 아닙니다.")
                return SkillResult.ERROR
            }

            val livingEntity: LivingEntity = target.bukkitEntity as LivingEntity
            livingEntity.damage(damage, owner)
            return SkillResult.SUCCESS
        } else {
            return SkillResult.CONDITION_FAILED
        }
    }
}
