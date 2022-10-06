package io.lumine.mythic.lib.damage;

import org.bukkit.entity.Entity;

public interface AttackHandler {

    /**
     * @param entity The entity being damaged by a specific plugin
     * @return Information about the attack (the potential player damage source,
     *         damage types, and attack damage value).
     */
    AttackMetadata getAttack(Entity entity);

    /**
     * @param entity The entity being damaged
     * @return If the entity is being damaged by a specific plugin
     */
    boolean isAttacked(Entity entity);
}
