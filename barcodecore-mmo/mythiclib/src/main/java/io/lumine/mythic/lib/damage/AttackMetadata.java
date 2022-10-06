package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.player.PlayerMetadata;
import io.lumine.mythic.lib.api.stat.StatMap;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;

/**
 * Instanced every time MythicLib detects and monitors one attack from one player.
 *
 * @author Indyuce
 */
public class AttackMetadata extends PlayerMetadata {
    private final DamageMetadata damage;

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
    public AttackMetadata(DamageMetadata damage, StatMap.CachedStatMap statMap) {
        super(statMap);

        Validate.notNull(damage, "Attack cannot be null");

        this.damage = damage;
    }

    /**
     * @return Information about the attack
     */
    public DamageMetadata getDamage() {
        return damage;
    }

    public AttackMetadata clone() {
        return new AttackMetadata(damage.clone(), getStats());
    }

    public void damage(LivingEntity target) {
        damage(target, true);
    }

    public void damage(LivingEntity target, boolean knockback) {
        MythicLib.plugin.getDamage().damage(this, target, knockback);
    }
}
