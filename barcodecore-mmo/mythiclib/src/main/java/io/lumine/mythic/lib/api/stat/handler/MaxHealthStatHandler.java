package io.lumine.mythic.lib.api.stat.handler;

import io.lumine.mythic.lib.api.stat.SharedStat;
import io.lumine.mythic.lib.api.stat.StatMap;
import org.bukkit.attribute.Attribute;

/**
 * This fixes an issue where, when the player's max health decreases
 * due to any stat update, the player's health remains stuck to a value
 * that is strictly greater than the player's current max health.
 * <p>
 * The health would only update when the player takes damage to go down
 * back to a legal value.
 * <p>
 * This class makes sure to set the player's health back to a legal value
 * whenever the player's max health is updated.
 *
 * @author indyuce
 */
public class MaxHealthStatHandler extends AttributeStatHandler {
    public MaxHealthStatHandler() {
        super(Attribute.GENERIC_MAX_HEALTH, SharedStat.MAX_HEALTH, false);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void runUpdate(StatMap stats) {

        // Do everything like normal
        super.runUpdate(stats);

        // Fix the player health
        double max = stats.getPlayerData().getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double fixed = Math.max(0, Math.min(max, stats.getPlayerData().getPlayer().getHealth()));
        stats.getPlayerData().getPlayer().setHealth(fixed);
    }
}
