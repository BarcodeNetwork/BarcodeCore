package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.StatMap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class MovementSpeedStatHandler implements StatHandler {

    @SuppressWarnings("deprecation")
    @Override
    public void runUpdate(StatMap stats) {
        AttributeInstance ins = stats.getPlayerData().getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        StatInstance statIns = stats.getInstance("MOVEMENT_SPEED");
        removeModifiers(ins);

        /*
         * Calculate speed malus reduction (capped at 80%)
         */
        double coef = 1 - Math.min(.8, Math.max(0, stats.getInstance("SPEED_MALUS_REDUCTION").getTotal() / 100));

        /*
         * This guarantees that weapons held in off
         * hand don't register any of their stats.
         */
        double d = statIns.getFilteredTotal(mod -> !mod.getSource().isWeapon() || mod.getSlot() != EquipmentSlot.OFF_HAND, mod -> mod.getValue() < 0 ? mod.multiply(coef) : mod);

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
        return map.getPlayerData().getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
    }
}
