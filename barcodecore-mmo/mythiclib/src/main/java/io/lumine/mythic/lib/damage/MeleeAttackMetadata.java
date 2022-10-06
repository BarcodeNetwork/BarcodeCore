package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.api.stat.StatMap;

/**
 * Used by melee attacks with melee weapons like custom
 * or vanilla swords, axes...
 */
public class MeleeAttackMetadata extends AttackMetadata {

    /**
     * Used by DamageHandler instances to register attacks. AttackResult only
     * gives information about the attack damage and types while this class also
     * contains info about the damager. Some plugins don't let MythicLib determine
     * what the damager is so there might be problem with damage/reduction stat
     * application.
     *
     * @param damage  The attack result
     * @param statMap The entity who dealt the damage
     */
    public MeleeAttackMetadata(DamageMetadata damage, StatMap.CachedStatMap statMap) {
        super(damage, statMap);
    }
}
