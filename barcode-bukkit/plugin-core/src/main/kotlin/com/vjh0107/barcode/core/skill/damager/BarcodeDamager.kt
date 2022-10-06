package com.vjh0107.barcode.core.skill.damager


import com.vjh0107.barcode.core.dependencies.mythicmobs.adapters.MythicMobsAdapter
import io.lumine.mythic.lib.api.player.EquipmentSlot
import io.lumine.mythic.lib.api.player.MMOPlayerData
import io.lumine.mythic.lib.damage.AttackMetadata
import io.lumine.mythic.lib.damage.DamageMetadata
import io.lumine.mythic.lib.damage.DamageType
import net.Indyuce.mmocore.MMOCore
import net.Indyuce.mmocore.skill.Skill
import net.Indyuce.mmoitems.api.player.PlayerData
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * @param attacker attacker as player
 * @param skillDamageParameter ex) 130.0 -> 데미지가 130%로 들어감
 * @param damageTypes list of enum class DamageType
 */
fun LivingEntity.giveBarcodeDamage(attacker: Player, skillDamageParameter: Double, damageTypes: Array<DamageType>) {
    val victim = this
    // 여기로 전문기술 몹은 예외처리한다.
    if (MythicMobsAdapter.inst().mobManager.getActiveMob(victim.uniqueId).isPresent) {
        val activeMob = MythicMobsAdapter.inst().mobManager.getActiveMob(victim.uniqueId).get()
        if (activeMob.type.config.getBoolean("Barcode.IsMeisterMob")) {
            return
        }
    }

    val mmoItemsPlayerData = PlayerData.get(attacker)
    val statMap = mmoItemsPlayerData.mmoPlayerData.statMap.cache(EquipmentSlot.ANY).data.statMap

    val damage = statMap.getStat("ATTACK_DAMAGE") * (skillDamageParameter / 100)

    val damageMetadata = DamageMetadata(damage, *damageTypes)
    val attackMetadata = AttackMetadata(damageMetadata, MMOPlayerData.get(attacker).statMap.cache(EquipmentSlot.ANY))

    victim.noDamageTicks = 0
    attackMetadata.damage(victim)
    attacker.playSound(attacker.location, Sound.ENTITY_PLAYER_HURT, 0.5F, 1F)
    victim.noDamageTicks = 0
}


fun Player.getSkillDamageModifier(skillName: String, skillDamageKey: String) : Double {
    val mmoCorePlayerData = net.Indyuce.mmocore.api.player.PlayerData.get(player)
    val skill: Skill = MMOCore.plugin.skillManager.get(skillName.uppercase())
        ?: throw NullPointerException("skillName: $skillName, skillDamageKey: $skillDamageKey")
    return mmoCorePlayerData
        .profess
        .getSkill(skillName.uppercase())
        .getModifier(skillDamageKey, mmoCorePlayerData.getSkillLevel(skill))
}