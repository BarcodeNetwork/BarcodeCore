package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.StatMap;
import io.lumine.mythic.lib.api.stat.modifier.ModifierSource;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class AttributeStatHandler implements StatHandler {
    private final Attribute attribute;
    private final String stat;
    private final boolean meleeWeaponStat;

    public AttributeStatHandler(Attribute attribute, String stat) {
        this(attribute, stat, false);
    }

    /**
     * Statistics like Atk Damage, Atk Speed, Max Health...
     * which are based on vanilla player attributes.
     *
     * @param attribute       The corresponding vanilla player attribute
     * @param stat            The stat identifier
     * @param meleeWeaponStat When set to true, stat modifiers from ranged weapons won't
     *                        be taken into account. This is only the case for Attack Damage
     *                        and Attack Speed
     */
    public AttributeStatHandler(Attribute attribute, String stat, boolean meleeWeaponStat) {
        this.attribute = attribute;
        this.stat = stat;
        this.meleeWeaponStat = meleeWeaponStat;
    }

    @Override
    public void runUpdate(StatMap stats) {
        AttributeInstance ins = stats.getPlayerData().getPlayer().getAttribute(attribute);
        StatInstance statIns = stats.getInstance(stat);
        removeModifiers(ins);

        /*
         * The first two boolean checks make sure that ranged
         * weapons do not register their attack damage.
         *
         * The last two checks guarantee that weapons
         * held in off hand don't register any of their stats.
         */
        double d = statIns.getFilteredTotal(mod -> (!meleeWeaponStat || mod.getSource() != ModifierSource.RANGED_WEAPON) &&
                (!mod.getSource().isWeapon() || mod.getSlot() != EquipmentSlot.OFF_HAND));

        /*
         * Calculate the stat base value. Since it can be changed by
         * external plugins, it's better to calculate it once and cache the result.
         */
        double base = statIns.getBase();

        /*
         * Only add an attribute modifier if the very final stat
         * value is different from the main one to save calculations.
         */
        if (d != base)
            ins.addModifier(new AttributeModifier("mythiclib.main", d - base, AttributeModifier.Operation.ADD_NUMBER));
    }

    @Override
    public double getBaseValue(StatMap map) {
        return map.getPlayerData().getPlayer().getAttribute(attribute).getBaseValue();
    }
}
