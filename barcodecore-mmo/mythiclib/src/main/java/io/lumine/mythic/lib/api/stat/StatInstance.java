package io.lumine.mythic.lib.api.stat;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.modifier.Closable;
import io.lumine.mythic.lib.api.stat.modifier.ModifierType;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class StatInstance {
    private final StatMap map;
    private final String stat;
    private final Map<String, StatModifier> modifiers = new HashMap<>();

    private static final Predicate<StatModifier> DEFAULT_MODIFIER_FILTER = mod -> !mod.getSource().isWeapon() || mod.getSlot() != EquipmentSlot.OFF_HAND;

    public StatInstance(StatMap map, String stat) {
        this.map = map;
        this.stat = stat;
    }

    public StatMap getMap() {
        return map;
    }

    public String getStat() {
        return stat;
    }

    public double getBase() {
        return MythicLib.inst().getStats().getBaseValue(stat, map);
    }

    /**
     * @return The final stat value taking into account the default stat value
     *         as well as the stat modifiers. The relative stat modifiers are
     *         applied afterwards, onto the sum of the base value + flat
     *         modifiers.
     */
    public double getTotal() {
        return getFilteredTotal(DEFAULT_MODIFIER_FILTER);
    }

    /**
     * @param filter
     *            Filters stat modifications taken into account for the calculation
     *
     * @return The final stat value taking into account the default stat value
     *         as well as the stat modifiers. The relative stat modifiers are
     *         applied afterwards, onto the sum of the base value + flat
     *         modifiers.
     */
    public double getFilteredTotal(Predicate<StatModifier> filter) {
        return getFilteredTotal(filter, mod -> mod);
    }

    /**
     * @param modification
     *            A modification to any stat modifier before taking it into
     *            account in stat calculation. This can be used for instance to
     *            reduce debuffs, by checking if a stat modifier has a negative
     *            value and returning a modifier with a reduced absolute value
     *
     * @return The final stat value taking into account the default stat value
     *         as well as the stat modifiers. The relative stat modifiers are
     *         applied afterwards, onto the sum of the base value + flat
     *         modifiers.
     */
    public double getTotal(Function<StatModifier, StatModifier> modification) {
        return getFilteredTotal(DEFAULT_MODIFIER_FILTER, modification);
    }

    /**
     * @param filter       Filters stat modifications taken into account for the calculation
     * @param modification A modification to any stat modifier before taking it into
     *                     account in stat calculation. This can be used for instance to
     *                     reduce debuffs, by checking if a stat modifier has a negative
     *                     value and returning a modifier with a reduced absolute value
     * @return The final stat value taking into account the default stat value
     *         as well as the stat modifiers. The relative stat modifiers are
     *         applied afterwards, onto the sum of the base value + flat
     *         modifiers.
     */
    public double getFilteredTotal(Predicate<StatModifier> filter, Function<StatModifier, StatModifier> modification) {
        double d = getBase();

        for (StatModifier attr : modifiers.values())
            if (attr.getType() == ModifierType.FLAT && filter.test(attr))
                d += modification.apply(attr).getValue();

        for (StatModifier attr : modifiers.values())
            if (attr.getType() == ModifierType.RELATIVE && filter.test(attr))
                d *= 1 + modification.apply(attr).getValue() / 100;

        return d;
    }

    /**
     * @param key The string key of the external modifier source or plugin
     * @return Attribute with the given key, or throws a NPE if not found
     * @deprecated use {@link StatInstance#getModifier(String)} instead
     */
    @Deprecated
    public StatModifier getAttribute(String key) {
        return modifiers.get(key);
    }

    /**
     * @param key The string key of the external modifier source or plugin
     * @return Attribute with the given key, or throws a NPE if not found
     */
    public StatModifier getModifier(String key) {
        return modifiers.get(key);
    }

    /**
     * Registers a stat modifier and run the required player stat updates
     *
     * @param key      The string key of the stat
     * @param modifier The stat modifier being registered
     */
    public void addModifier(String key, StatModifier modifier) {
        modifiers.put(key, modifier);

        MythicLib.plugin.getStats().runUpdate(map, stat);
    }

    /**
     * @return All registered stat modifiers
     */
    public Collection<StatModifier> getModifiers() {
        return modifiers.values();
    }

    /**
     * @return All string keys of currently registered stat modifiers
     */
    public Set<String> getKeys() {
        return modifiers.keySet();
    }

    /**
     * Iterates through registered stat modifiers and unregisters them if a
     * certain condition based on their string key is met
     *
     * @param condition
     *            Condition on the modifier key, if it should be unregistered or
     *            not
     */
    public void removeIf(Predicate<String> condition) {
        boolean check = false;

        for (Iterator<Map.Entry<String, StatModifier>> iterator = modifiers.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String, StatModifier> entry = iterator.next();
            if (condition.test(entry.getKey())) {

                StatModifier modifier = entry.getValue();
                if (modifier instanceof Closable)
                    ((Closable) modifier).close();

                iterator.remove();
                check = true;
            }
        }

        if (check)
            MythicLib.plugin.getStats().runUpdate(map, stat);
    }

    /**
     * @param key
     *            The string key of the external stat modifier source or plugin
     * @return If a stat modifier is registered with this key
     */
    public boolean contains(String key) {
        return modifiers.containsKey(key);
    }

    /**
     * Removes a stat modifier with a specific key
     *
     * @param key
     *            The string key of the external stat modifier source or plugin
     */
    public void remove(String key) {

        if (!modifiers.containsKey(key))
            return;

        /**
         * Closing modifier is really important with temporary stats because
         * otherwise the runnable will try to remove the key from the map even
         * though the attribute was cancelled before hand
         */
        StatModifier mod = modifiers.get(key);
        if (mod instanceof Closable)
            ((Closable) mod).close();

        modifiers.remove(key);
        MythicLib.plugin.getStats().runUpdate(map, stat);
    }

    public ModifierPacket newPacket() {
        return new ModifierPacket();
    }

    /**
     * Allows to first add as many modifiers as needed and only THEN update the
     * stat instance to avoid sending too many udpates at one time which can
     * be performance heavy for attribute based stats.
     *
     * @author indyuce
     */
    public class ModifierPacket {

        /**
         * Registers a stat modifier and run the required player stat updates
         *
         * @param key      The string key of the stat
         * @param modifier The stat modifier being registered
         */
        public void addModifier(String key, StatModifier modifier) {
            modifiers.put(key, modifier);
        }

        /**
         * Iterates through registered stat modifiers and unregisters them if a
         * certain condition based on their string key is met
         *
         * @param condition Condition on the modifier key, if it should be unregistered or
         *                  not
         */
        public void removeIf(Predicate<String> condition) {
            for (Iterator<Map.Entry<String, StatModifier>> iterator = modifiers.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, StatModifier> entry = iterator.next();
                if (condition.test(entry.getKey())) {

                    StatModifier modifier = entry.getValue();
                    if (modifier instanceof Closable)
                        ((Closable) modifier).close();

                    iterator.remove();
                }
            }
        }

        public void runUpdate() {
            MythicLib.plugin.getStats().runUpdate(map, stat);
        }
    }
}

