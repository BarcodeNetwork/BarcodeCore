package io.lumine.mythic.lib.comp.flags;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface FlagPlugin {
    boolean isPvpAllowed(Location loc);

    boolean isFlagAllowed(Player player, CustomFlag flag);

    boolean isFlagAllowed(Location loc, CustomFlag flag);
}
