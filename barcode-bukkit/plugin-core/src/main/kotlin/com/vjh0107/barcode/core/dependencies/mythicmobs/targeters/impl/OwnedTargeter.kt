package com.vjh0107.barcode.core.dependencies.mythicmobs.targeters.impl

import com.vjh0107.barcode.core.dependencies.mythicmobs.targeters.BarcodeEntitySelector
import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.core.mobs.ActiveMob

class OwnedTargeter(mlc: MythicLineConfig) : BarcodeEntitySelector(mlc) {
    override fun getEntities(data: SkillMetadata): java.util.HashSet<AbstractEntity?> {
        val targets: java.util.HashSet<AbstractEntity?> = java.util.HashSet()
        val var3: Iterator<*> = MythicBukkit.inst().entityManager.getLivingEntities(data.caster.entity.world).iterator()
        val casterPlayer = data.caster.entity.bukkitEntity

        while (var3.hasNext()) {
            val p = var3.next() as AbstractEntity
            val am: ActiveMob? = MythicBukkit.inst().mobManager.getMythicMobInstance(p) ?: null
            if (am != null) {
                if (data.caster.location.world == p.world && casterPlayer.uniqueId == am.owner.get() && p.uniqueId != data.caster.entity.uniqueId) {
                    targets.add(p)
                }
            }
        }
        return targets
    }
}
