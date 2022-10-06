package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.AttackHandler;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import org.apache.commons.lang.Validate;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;

public class DamageManager implements Listener, AttackHandler {
    private final Map<Integer, AttackMetadata> customDamage = new HashMap<>();
    private final Set<AttackHandler> handlers = new HashSet<>();

    private static final AttributeModifier noKnockback = new AttributeModifier(UUID.randomUUID(), "noKnockback", 100, AttributeModifier.Operation.ADD_NUMBER);

    public DamageManager() {
        handlers.add(this);
    }

    /**
     * Damage handlers are used by MythicLib to keep track of details of every
     * attack so that it can apply damage based stats like PvE damage, Magic
     * Damage...
     *
     * @param handler Damage handler being registered
     */
    public void registerHandler(AttackHandler handler) {
        Validate.notNull(handler, "Damage handler cannot be null");
        handlers.add(handler);
    }

    @Override
    public boolean isAttacked(Entity entity) {
        return customDamage.containsKey(entity.getEntityId());
    }

    @Override
    public AttackMetadata getAttack(Entity entity) {
        return customDamage.get(entity.getEntityId());
    }

    /**
     * Forces a player to damage an entity with knockback
     *
     * @param player The player damaging the entity
     * @param target The entity being damaged
     * @param result Info about the attack. Since this attack is registered as
     *               MythicLib damage, we need more than just a double for the atk
     *               damage
     */
    @Deprecated
    public void damage(Player player, LivingEntity target, DamageMetadata result) {
        damage(player, target, result, true);
    }

    /**
     * Forces a player to damage an entity with or without knockback
     *
     * @param player    The player damaging the entity
     * @param target    The entity being damaged
     * @param result    Info about the attack. Since this attack is registered as
     *                  MythicLib damage, we need more than just a double for the atk
     *                  damage
     * @param knockback If the attack should inflict knockback
     */
    @Deprecated
    public void damage(@NotNull Player player, @NotNull LivingEntity target, @NotNull DamageMetadata result, boolean knockback) {
        AttackMetadata metadata = new AttackMetadata(result, MMOPlayerData.get(player).getStatMap().cache(EquipmentSlot.MAIN_HAND));
        damage(metadata, target, true);
    }

    /**
     * Forces a player to damage an entity with knockback
     *
     * @param metadata The class containing all info about the current attack
     * @param target   The entity being damaged
     */
    public void damage(@NotNull AttackMetadata metadata, @NotNull LivingEntity target) {
        damage(metadata, target, true);
    }

    /**
     * Forces a player to damage an entity with (no) knockback
     *
     * @param metadata  The class containing all info about the current attack
     * @param target    The entity being damaged
     * @param knockback If the attack should deal knockback
     */
    public void damage(@NotNull AttackMetadata metadata, @NotNull LivingEntity target, boolean knockback) {
        damage(metadata, target, knockback, false);
    }

    /**
     * Forces a player to damage an entity.
     *
     * @param metadata  The class containing all info about the current attack
     * @param target    The entity being damaged
     * @param knockback If the attack should deal knockback
     * @param ignoreImmunity The attack will not produce immunity frames.
     */
    public void damage(@NotNull AttackMetadata metadata, @NotNull LivingEntity target, boolean knockback, boolean ignoreImmunity) {
        if (target.hasMetadata("NPC") || metadata.getPlayer().hasMetadata("NPC"))
            return;

        // Register custom damage
        customDamage.put(target.getEntityId(), metadata);

        /*
         * No knockback: temporarily apply a 100% knockback resistance
         * to the target before applying regular damage
         */
        if (!knockback)
            try {
                target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).addModifier(noKnockback);
                target.damage(metadata.getDamage().getDamage(), metadata.getPlayer());
            } catch (Exception anyError) {
                MythicLib.plugin.getLogger().log(Level.WARNING, "An error occured while registering player damage");
                anyError.printStackTrace();
            } finally {
                target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).removeModifier(noKnockback);
            }

            // Apply default knockback
        else
            target.damage(metadata.getDamage().getDamage(), metadata.getPlayer());

        // No damage immunity
        if (ignoreImmunity)
            target.setNoDamageTicks(0);
    }

    /**
     * @param entity The entity to check
     * @return Null if the entity is being damaged through vanilla
     *         actions, or the corresponding RegisteredAttack if MythicLib
     *         found a plugin responsible for that damage
     */
    public AttackMetadata findInfo(Entity entity) {
        for (AttackHandler handler : handlers)
            if (handler.isAttacked(entity))
                return handler.getAttack(entity);
        return null;
    }

    /**
     * This method is used to unregister MythicLib custom damage after everything
     * was calculated, hence MONITOR priority
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void unregisterCustomDamage(EntityDamageByEntityEvent event) {
        customDamage.remove(event.getEntity().getEntityId());
    }
}
