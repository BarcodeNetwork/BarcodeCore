package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.api.event.PlayerKillEntityEvent;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.util.ProjectileTrigger;
import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SkillTriggers implements Listener {

    @EventHandler
    public void killEntity(PlayerKillEntityEvent event) {
        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        caster.triggerSkills(TriggerType.KILL_ENTITY, event.getTarget());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void attack(PlayerAttackEvent event) {
        event.getData().triggerSkills(TriggerType.ATTACK, event.getAttack(), event.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void damagedByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && MMOPlayerData.has(event.getEntity().getUniqueId())
                && MythicLib.plugin.getEntities().canTarget((Player) event.getEntity(), event.getDamager(), InteractionType.OFFENSE_SKILL)) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            caster.triggerSkills(TriggerType.DAMAGED_BY_ENTITY, event.getDamager());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void damaged(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && MMOPlayerData.has(event.getEntity().getUniqueId())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            caster.triggerSkills(TriggerType.DAMAGED, null);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void death(PlayerDeathEvent event) {
        if (MMOPlayerData.has(event.getEntity())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            caster.triggerSkills(TriggerType.DEATH, null);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void login(PlayerJoinEvent event) {
        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        caster.triggerSkills(TriggerType.LOGIN, null);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void shootBow(EntityShootBowEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && MMOPlayerData.has(event.getEntity().getUniqueId())) {
            MMOPlayerData caster = MMOPlayerData.get(event.getEntity().getUniqueId());
            caster.triggerSkills(TriggerType.SHOOT_BOW, event.getProjectile());

            // Register a runnable to trigger projectile skills
            EquipmentSlot hand = getShootHand(((Player) event.getEntity()).getInventory());
            new ProjectileTrigger(caster, ProjectileTrigger.ProjectileType.ARROW, event.getProjectile(), hand);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void shootTrident(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Trident && event.getEntity().getShooter() instanceof Player && MMOPlayerData.has((Player) event.getEntity().getShooter())) {
            Player shooter = (Player) event.getEntity().getShooter();
            MMOPlayerData caster = MMOPlayerData.get(shooter);
            caster.triggerSkills(TriggerType.SHOOT_TRIDENT, event.getEntity());

            // Register a runnable to trigger projectile skills
            EquipmentSlot hand = getShootHand(shooter.getInventory());
            new ProjectileTrigger(caster, ProjectileTrigger.ProjectileType.TRIDENT, event.getEntity(), hand);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void sneak(PlayerToggleSneakEvent event) {
        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        caster.triggerSkills(TriggerType.SNEAK, null);
    }

    /**
     * {@link Cancellable#isCancelled()} does not work with PlayerInteractEvent
     * because there are now two possible ways to cancel the event, either
     * by canceling the item interaction, either by canceling the block interaction.
     * <p>
     * Checking if the event is cancelled points towards the block interaction
     * and not the item interaction which is NOT what MythicLib is interested in
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void click(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL || event.useItemInHand() == Event.Result.DENY)
            return;

        MMOPlayerData caster = MMOPlayerData.get(event.getPlayer());
        boolean left = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK, sneaking = event.getPlayer().isSneaking();
        TriggerType type = sneaking ? (left ? TriggerType.SHIFT_LEFT_CLICK : TriggerType.SHIFT_RIGHT_CLICK) : (left ? TriggerType.LEFT_CLICK : TriggerType.RIGHT_CLICK);
        caster.triggerSkills(type, null);
    }

    /**
     * @return Hand used to shoot a projectile (arrow/trident) based on
     *         what items the player is holding in his two hands
     */
    private EquipmentSlot getShootHand(PlayerInventory inv) {
        ItemStack main = inv.getItemInMainHand();
        return main != null && isShootable(main.getType()) ? EquipmentSlot.MAIN_HAND : EquipmentSlot.OFF_HAND;
    }

    private boolean isShootable(Material mat) {
        return mat == Material.BOW || mat == Material.CROSSBOW || mat == Material.TRIDENT;
    }
}
