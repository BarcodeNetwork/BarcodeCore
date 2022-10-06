package io.lumine.mythic.lib.listener.event;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.event.PlayerKillEntityEvent;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.damage.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

/**
 * First problem: you want to create a skill which does something whenever
 * a player attacks a given entity, but you have to listen to all the attack
 * types: ranged attacks with bows, melee attacks with sword... and even abilities.
 * We need some event to generalize every attack, which is {@link PlayerAttackEvent}
 * <p>
 * A PlayerAttackEvent is called WHENEVER a player attacks, by any way, an entity.
 * This is super useful for MMOCore skills and other external plugins.
 * <p>
 * Second problem: if a player shoots another entity, it's not hard to get the
 * damaging entity, the arrow and trace back its shooter. However, if an external
 * plugin damages an entity WITHOUT telling Spigot that the player is the damage
 * source, it's IMPOSSIBLE to trace back the initial damager.
 * <p>
 * DamageManager gives a way to let MythicLib know that some player damaged some
 * entity. Basically MythicLib is monitoring every single attack from every single
 * player to keep track 1) of the initial caster, 2) of the damage types, that
 * is whether it is a SKILL or WEAPON attack.
 *
 * @author indyuce
 */
public class PlayerAttackEventListener implements Listener {

    /**
     * Calls a PlayerAttackEvent whenever an entity is attacked, only if
     * MythicLib manages to find
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void registerEvents(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Damageable) || event.getDamage() == 0)
            return;

        /*
         * Looks for a RegisteredAttack. If it 1) can't find one registered in the {@link DamageManager}
         * and if MythicLib 2) cannot generate one specifically for this damage event, the
         * MythicLib will NOT monitor this attack i.e NEITHER apply damage stats NOR call PlayerAttackEvent.
         */
        AttackMetadata attack = findAttack(event);
        if (attack == null)
            return;

        /*
         * Call the Bukkit event with the attack meta found
         */
        MMOPlayerData playerData = MMOPlayerData.get(attack.getPlayer());
        PlayerAttackEvent attackEvent = new PlayerAttackEvent(playerData, event, attack);
        Bukkit.getPluginManager().callEvent(attackEvent);
        if (attackEvent.isCancelled())
            return;

        event.setDamage(attack.getDamage().getDamage());

        /*
         * Call the death event if the entity is being killed
         */
        if (event.getFinalDamage() >= ((Damageable) event.getEntity()).getHealth()) {
            Bukkit.getPluginManager().callEvent(new PlayerKillEntityEvent(playerData, attack, (LivingEntity) event.getEntity()));
        }
    }

    /**
     * Very important method. Looks for a RegisteredAttack that would have been registered
     * by other plugins ie MMOItems abilities, or MythicCore abilities, or any other plugin.
     * <p>
     * If it can't find any plugin that has registered an attack, it checks if it is simply
     * not just a melee attack (then a
     */
    private AttackMetadata findAttack(EntityDamageByEntityEvent event) {

        /*
         * Checks in the MythicLib registered attack. This is used by MMOItems skills,
         * MMOCore skills, or any other plugin that implement MythicLib compatibility.
         */
        AttackMetadata custom = MythicLib.plugin.getDamage().findInfo(event.getEntity());
        if (custom != null)
            return custom;

        // Players damaging Citizens NPCs are not registered
        if (event.getEntity().hasMetadata("NPC"))
            return null;

        /*
         * Handles melee attacks. This is used everytime a player left clicks an entity.
         *
         * The attack damage type can vary depending on the context: if it is a bare-firsts
         * attack, final attack has no WEAPON damage type. If the player is holding any
         * other item, it is considered a WEAPON attack.
         */
        if (isValidPlayer(event.getDamager()))
            return new MeleeAttackMetadata(new DamageMetadata(event.getDamage(), getDamageTypes(event)), MMOPlayerData.get((Player) event.getDamager()).getStatMap().cache(EquipmentSlot.MAIN_HAND));

        /*
         * Handles projectile attacks; used everytime when a player shoots a trident,
         * a bow, a crossbow or even eggs and snowballs.
         *
         * Notice this is always the same damage type: WEAPON, PHYSICAL, PROJECTILE
         * which means that if MMOCore has a skill which makes players shoot multiple
         * arrows, MythicLib will use the following lines to monitor the attacks
         * and the skill will apply WEAPON damage.
         *
         * Make sure to check the shooter is not the damaged entity. We don't want
         * players to backstab themselves using projectiles.
         */
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            ProjectileSource source = projectile.getShooter();
            if (source == null)
                return null;

            if (!source.equals(event.getEntity()) && isValidPlayer(source))
                return new ProjectileAttackMetadata(new DamageMetadata(event.getDamage(), DamageType.WEAPON, DamageType.PHYSICAL, DamageType.PROJECTILE),
                        MMOPlayerData.get((Player) source).getStatMap().cache(EquipmentSlot.MAIN_HAND), projectile);
        }

        return null;
    }

    /**
     * @return If the entity is a player and NOT a Citizens or Sentinels NPC
     */
    private boolean isValidPlayer(Object entity) {
        return entity instanceof Player && !((Player) entity).hasMetadata("NPC");
    }

    /**
     * @param event The attack event
     * @return The damage types of a vanilla melee entity attack
     */
    private DamageType[] getDamageTypes(EntityDamageByEntityEvent event) {

        /*
         * If the attacker has no item in his hand when attacking, attack is
         * only physical
         */
        if (event.getDamager() instanceof LivingEntity) {
            LivingEntity damager = (LivingEntity) event.getDamager();
            if (isAir(damager.getEquipment().getItemInMainHand()))
                return new DamageType[]{DamageType.PHYSICAL};
        }

        /*
         * By default a physical attack is a weapon-physical attack
         */
        return new DamageType[]{DamageType.WEAPON, DamageType.PHYSICAL};
    }

    private boolean isAir(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
}

