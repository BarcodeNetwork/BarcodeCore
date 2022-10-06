package io.lumine.mythic.lib.listener;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.math.EvaluatedFormula;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.damage.AttackMetadata;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.logging.Level;

public class DamageReduction implements Listener {

    /**
     * Since MythicMobs is a soft depend, this event triggers
     * correctly, fixing a bug with MythicMobs skill mechanics.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void damageMitigation(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getEntity().hasMetadata("NPC"))
            return;

        // Get sweet data
        MMOPlayerData data = MMOPlayerData.get((OfflinePlayer) event.getEntity());
        AttackMetadata attackMeta = MythicLib.plugin.getDamage().findInfo(event.getEntity());
        DamageMetadata damageMeta = attackMeta == null ? new DamageMetadata(event.getDamage(), DamageType.WEAPON, DamageType.PHYSICAL) : attackMeta.getDamage();

        // Applies specific damage reduction
        for (DamageReductionType type : DamageReductionType.values())
            type.applyReduction(data.getStatMap(), damageMeta, event);

        // Applies the Defense stat
        double defense = data.getStatMap().getStat("DEFENSE");
        double damage = damageMeta.getDamage();
        if (defense > 0)
            damage = new DefenseFormula(defense).getAppliedDamage(damage);

        // Finally apply damage
        event.setDamage(damage);
    }

    /**
     * Used for calculating damage mitigation due to the defense stat.
     */
    public class DefenseFormula {
        private final double defense;

        public DefenseFormula(double defense) {
            this.defense = defense;
        }

        public double getAppliedDamage(double damage) {
            String formula = MythicLib.plugin.getConfig().getString("defense-application", "#damage# * (1 - (#defense# / (#defense# + 100)))");
            formula = formula.replace("#defense#", String.valueOf(defense));
            formula = formula.replace("#damage#", String.valueOf(damage));

            try {
                return Math.max(0, new EvaluatedFormula(formula).evaluate());
            } catch (RuntimeException exception) {

                /**
                 * Formula won't evaluate if hanging #'s or unparsed placeholders. Send a
                 * friendly warning to console and just return the default damage.
                 */
                MythicLib.inst().getLogger()
                        .log(Level.WARNING, "Could not evaluate defense formula, please check config.");
                return damage;
            }
        }
    }

    /**
     * All different types of damage reduction.
     */
    public enum DamageReductionType {

        // Damage reduction, always applies
        ENVIRONMENTAL("DAMAGE_REDUCTION", null, event -> true),

        // Vanilla damage types
        PVP(event -> event instanceof EntityDamageByEntityEvent && getDamager((EntityDamageByEntityEvent) event) instanceof Player),
        PVE(event -> event instanceof EntityDamageByEntityEvent && !(getDamager((EntityDamageByEntityEvent) event) instanceof Player)),
        FIRE(event -> event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK),
        FALL(event -> event.getCause() == EntityDamageEvent.DamageCause.FALL),

        // Custom damage types
        MAGIC(DamageType.MAGIC, event -> event.getCause() == EntityDamageEvent.DamageCause.MAGIC),
        PHYSICAL(DamageType.PHYSICAL, event -> event instanceof EntityDamageByEntityEvent),
        WEAPON(DamageType.WEAPON),
        SKILL(DamageType.SKILL),
        PROJECTILE(DamageType.PROJECTILE, event -> event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Projectile);

        /**
         * The corresponding item stat that will be used to
         * apply damage reduction. For instance, ENVIRONMENTAL calls
         * DAMAGE_REDUCTION and MAGIC calls MAGIC_DAMAGE_REDUCTION
         */
        private final String stat;

        /**
         * When this field is not null, that means this damage reduction is linked
         * to a specific damage type. For instance, {@link #MAGIC} should reduce
         * magic damage, etc.
         */
        @Nullable
        private final DamageType damageType;

        /**
         * When this field is not null, if it does return true, it will reduce
         * all the damage from every damage packet. This is used for vanilla
         * damage types, like {@link #FALL} or {@link #FIRE} or even {@link #ENVIRONMENTAL}
         */
        @Nullable
        private final Predicate<EntityDamageEvent> apply;

        private DamageReductionType(DamageType damageType) {
            this(damageType, null);
        }

        private DamageReductionType(Predicate<EntityDamageEvent> apply) {
            this(null, apply);
        }

        private DamageReductionType(DamageType damageType, Predicate<EntityDamageEvent> apply) {
            this.stat = name() + "_DAMAGE_REDUCTION";
            this.damageType = damageType;
            this.apply = apply;
        }

        private DamageReductionType(String stat, DamageType damageType, Predicate<EntityDamageEvent> apply) {
            this.stat = stat;
            this.damageType = damageType;
            this.apply = apply;
        }

        public void applyReduction(StatMap statMap, DamageMetadata damageMeta, EntityDamageEvent event) {

            // Environmental damage reduction
            if (apply != null && apply.test(event))
                damageMeta.multiply(getCoefficient(statMap));

                // Specific damage type reduction
            else if (damageType != null)
                damageMeta.multiply(getCoefficient(statMap), damageType);
        }

        private double getCoefficient(StatMap statMap) {
            return 1 - Math.max(0, Math.min(1, statMap.getStat(stat) / 100));
        }
    }

    /**
     * Tries to find the entity who dealt the damage in some attack event. Main issue is that
     * if it is a ranged attack like a trident or an arrow, we have to find back the shooter.
     */
    private static LivingEntity getDamager(EntityDamageByEntityEvent event) {

        /*
         * Check direct damager
         */
        if (event.getDamager() instanceof LivingEntity)
            return (LivingEntity) event.getDamager();

        /*
         * Checks projectile and add damage type, which supports every vanilla
         * projectile like snowballs, tridents and arrows
         */
        if (event.getDamager() instanceof Projectile) {
            Projectile proj = (Projectile) event.getDamager();
            if (proj.getShooter() instanceof LivingEntity)
                return (LivingEntity) proj.getShooter();
        }

        return null;
    }
}
