package io.lumine.mythic.lib.api.stat;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.provider.StatProvider;
import io.lumine.mythic.lib.damage.DamageMetadata;
import io.lumine.mythic.lib.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO create an abstract type of 'StatMap' or 'StatProvider'
 * which the default player stat map and the cached statMap
 * both extend
 *
 * @author indyuce
 */
public class StatMap implements StatProvider {
    private final MMOPlayerData data;
    private final Map<String, StatInstance> stats = new ConcurrentHashMap<>();

    public StatMap(MMOPlayerData player) {
        this.data = player;
    }

    /**
     * @return The StatMap owner ie the corresponding MMOPlayerData
     */
    public MMOPlayerData getPlayerData() {
        return data;
    }

    /**
     * @param stat The string key of the stat
     * @return The value of the stat after applying stat modifiers
     */
    @Override
    public double getStat(String stat) {
        return getInstance(stat).getTotal();
    }

    /**
     * @param id The string key of the stat
     * @return The corresponding StatInstance, which can be manipulated to add
     *         (temporary?) stat modifiers to a player, remove modifiers or
     *         calculate stat values in various ways. StatInstances are
     *         completely flushed when the server restarts
     */
    public StatInstance getInstance(String id) {
        if (stats.containsKey(id))
            return stats.get(id);

        StatInstance ins = new StatInstance(this, id);
        stats.put(id, ins);
        return ins;
    }

    /**
     * @return The StatInstances that have been manipulated so far since the
     *         player has logged in. StatInstances are completely flushed when
     *         the server restarts
     */
    public Collection<StatInstance> getInstances() {
        return stats.values();
    }

    /**
     * Some stats like movement speed, attack damage.. are based on vanilla
     * player attributes. Every time a stat modifier is added to a StatInstance
     * in MythicLib, MythicLib needs to perform a further attribute modifier update.
     * This method runs all the updates for the vanilla-attribute-based MythicLib
     * stats.
     * <p>
     * Performance heavy method
     */
    @Deprecated
    public void updateAll() {
        MythicLib.plugin.getStats().runUpdates(this);
    }

    /***
     * Runs a specific stat update for a specific StatMap
     *
     * @param stat
     *            The string key of the stat which needs update
     */
    @Deprecated
    public void update(String stat) {
        MythicLib.plugin.getStats().runUpdate(this, stat);
    }

    /**
     * @param castHand The casting hand matters a lot! Should MythicLib take into account
     *                 the 'Skill Damage' due to the offhand weapon, when casting a
     *                 skill with mainhand?
     * @return Some actions require the player stats to be temporarily saved.
     *         When a player casts a projectile skill, there's a brief delay
     *         before it hits the target: the stat values taken into account
     *         correspond to the stat values when the player cast the skill (not
     *         when it finally hits the target). This cache technique fixes a
     *         huge game breaking glitch
     */
    public CachedStatMap cache(EquipmentSlot castHand) {
        return new CachedStatMap(castHand);
    }

    public class CachedStatMap implements StatProvider {
        private final Player player;
        private final Map<String, Double> cached = new HashMap<>();

        private CachedStatMap(EquipmentSlot castSlot) {
            this.player = data.getPlayer();

            /*
             * When casting a skill or an attack with a certain hand, stats
             * from the other hand shouldn't be taken into account
             */
            if (castSlot.isHand()) {
                EquipmentSlot ignored = castSlot.getOppositeHand();
                for (StatInstance ins : getInstances())
                    this.cached.put(ins.getStat(), ins.getFilteredTotal(mod -> mod.getSlot() != ignored));

                /*
                 * Not casting the attack with a specific
                 * hand so take everything into account
                 */
            } else
                for (StatInstance ins : getInstances())
                    this.cached.put(ins.getStat(), ins.getTotal());
        }

        /**
         * @return The cached Player instance. Player instances are cached so
         *         that even if the player logs out, the ability can still be
         *         cast without additional errors
         */
        public Player getPlayer() {
            return player;
        }

        public MMOPlayerData getData() {
            return data;
        }

        /**
         * @param stat The string key of the stat
         * @return The cached stat value, or the vanilla
         */
        @Override
        public double getStat(String stat) {
            return cached.getOrDefault(stat, getInstance(stat).getBase());
        }

        /**
         * Edits the current cached stat value
         *
         * @param stat  The string key of the stat
         * @param value The value you want to cache
         */
        public void setStat(String stat, double value) {
            cached.put(stat, value);
        }

        public void damage(LivingEntity target, double value, DamageType... types) {
            MythicLib.plugin.getDamage().damage(player, target, new DamageMetadata(value, types));
        }
    }
}
