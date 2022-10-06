package io.lumine.mythic.lib.skill.metadata;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class TriggerMetadata {
    private final AttackMetadata attack;
    private final Entity target;

    /**
     * Instantiated every time a player performs an action linked
     * to a skill trigger. This is used to temporarily cache the player stats
     * and save the info needed to cast some skills
     *
     * @param attack Either the current attackMeta when the trigger type is DAMAGE for instance,
     *               or an empty one for any other trigger type.
     * @param target Potential skill target
     */
    public TriggerMetadata(AttackMetadata attack, Entity target) {
        this.attack = attack;
        this.target = target;
    }

    public AttackMetadata getAttack() {
        return attack;
    }

    public Entity getTarget() {
        return target;
    }

    /**
     * Utility method that makes a player deal damage to a specific
     * entity. This creates the attackMetadata based on the data
     * stored by the CasterMetadata, and calls it using MythicLib
     * damage manager
     *
     * @param target Target entity
     * @param damage Damage dealt
     * @param types  Type of target
     * @return The (modified) attack metadata
     */
    public AttackMetadata attack(LivingEntity target, double damage, DamageType... types) {
        AttackMetadata attackMeta = new AttackMetadata(new DamageMetadata(damage, types), attack.getStats());
        MythicLib.plugin.getDamage().damage(attackMeta, target);
        return attackMeta;
    }
}
