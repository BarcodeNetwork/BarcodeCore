package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.event.PlayerAttackEvent;
import io.lumine.mythic.lib.player.cooldown.CooldownType;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.damage.DamageType;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Random;

public class AttackEffects implements Listener {

    // Critical strike configs
    private double weaponCritCoef, skillCritCoef, maxWeaponCritChance, maxSkillCritChance, weaponCritCooldown, skillCritCooldown;

    private static final Random random = new Random();

    public AttackEffects() {
        reload();
    }

    public void reload() {
        weaponCritCoef = MythicLib.plugin.getConfig().getDouble("critical-strikes.weapon.coefficient", 2);
        skillCritCoef = MythicLib.plugin.getConfig().getDouble("critical-strikes.skill.coefficient", 1.5);

        maxWeaponCritChance = MythicLib.plugin.getConfig().getDouble("critical-strikes.weapon.max-chance", 80);
        maxSkillCritChance = MythicLib.plugin.getConfig().getDouble("critical-strikes.skill.max-chance", 80);

        weaponCritCooldown = MythicLib.plugin.getConfig().getDouble("critical-strikes.weapon.cooldown", 3);
        skillCritCooldown = MythicLib.plugin.getConfig().getDouble("critical-strikes.skill.cooldown", 3);
    }

    /**
     * Read {@link io.lumine.mythic.lib.listener.event.PlayerAttackEventListener}
     * <p>
     * See how easy it is to just listen to any player
     * attack and apply on-hit attack effects now??
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onHitAttackEffects(PlayerAttackEvent event) {
        StatMap stats = event.getData().getStatMap();

        // Apply specific damage increase
        for (DamageType type : DamageType.values())
            event.getDamage().multiply(Math.max(0, 1 + stats.getStat(type.getOffenseStat()) / 100), type);

        // Apply PvP or PvE damage, one of the two anyways.
        double versusDamageCoef = 1 + stats.getStat(event.getEntity() instanceof Player ? "PVP_DAMAGE" : "PVE_DAMAGE") / 100;
        event.getDamage().multiply(versusDamageCoef);

        // Weapon critical strikes
        if (event.getDamage().hasType(DamageType.WEAPON)
                && random.nextDouble() <= Math.min(stats.getStat("CRITICAL_STRIKE_CHANCE"), maxWeaponCritChance) / 100
                && !event.getData().isOnCooldown(CooldownType.WEAPON_CRIT)) {
            event.getData().applyCooldown(CooldownType.WEAPON_CRIT, weaponCritCooldown);
            event.getDamage().multiply(Math.max(0, weaponCritCoef + stats.getStat("CRITICAL_STRIKE_POWER") / 100), DamageType.WEAPON);
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 1);
            event.getEntity().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, event.getEntity().getLocation().add(0, 1, 0), 16, 0, 0, 0, .1);
        }

        // Skill critical strikes
        if (event.getDamage().hasType(DamageType.SKILL)
                && random.nextDouble() <= Math.min(stats.getStat("SPELL_CRITICAL_STRIKE_CHANCE"), maxSkillCritChance) / 100
                && !event.getData().isOnCooldown(CooldownType.SKILL_CRIT)) {
            event.getData().applyCooldown(CooldownType.SKILL_CRIT, skillCritCooldown);
            event.getDamage().multiply(skillCritCoef + stats.getStat("SPELL_CRITICAL_STRIKE_POWER") / 100, DamageType.SKILL);
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 2);
            event.getEntity().getWorld().spawnParticle(Particle.TOTEM, event.getEntity().getLocation().add(0, 1, 0), 32, 0, 0, 0, .4);
        }
    }
}
