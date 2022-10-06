package net.Indyuce.mmoitems.stat;

import io.lumine.mythic.lib.version.VersionMaterial;
import net.Indyuce.mmoitems.stat.data.StringData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ChooseStat;
import net.Indyuce.mmoitems.stat.type.GemStoneStat;
import org.jetbrains.annotations.NotNull;

/**
 * Defines how gem stats will scale when the item they are put on upgrades.
 */
public class GemUpgradeScaling extends ChooseStat implements GemStoneStat {
    public static final String NEVER = "NEVER", HISTORIC = "HISTORIC", SUBSEQUENT = "SUBSEQUENT";

    /**
     * Can't be final as it is a plugin configuration option
     */
    public static String defaultValue = SUBSEQUENT;

    public GemUpgradeScaling() {
        super("GEM_UPGRADE_SCALING", VersionMaterial.LIME_DYE.toMaterial(), "Gem Upgrade Scaling", new String[] { "Gem stones add their stats to items, but you may also", "upgrade your items via crafting stations or consumables.", "", "\u00a76Should this gem stone stats be affected by upgrading?" }, new String[] { "gem_stone" });

        // Set the acceptable values
        addChoices(SUBSEQUENT, NEVER, HISTORIC);

        // Put definitions
        setHint(SUBSEQUENT, "Gem stats scale by upgrading the item, but only after putting the gem in.");
        setHint(NEVER, "Gem stats are never scaled by upgrading the item.");
        setHint(HISTORIC, "Gem stats instantly upgrade to the current item level, and subsequently thereafter.");
    }

    @NotNull @Override public StatData getClearStatData() { return new StringData(defaultValue); }
}
