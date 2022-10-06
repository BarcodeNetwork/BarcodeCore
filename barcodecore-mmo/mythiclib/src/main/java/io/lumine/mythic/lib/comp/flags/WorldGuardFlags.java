package io.lumine.mythic.lib.comp.flags;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WorldGuardFlags implements FlagPlugin {
    private final WorldGuard worldguard;
    private final WorldGuardPlugin worldguardPlugin;
    private final Map<String, StateFlag> flags = new HashMap<>();

    public WorldGuardFlags() {
        this.worldguard = WorldGuard.getInstance();
        this.worldguardPlugin = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        FlagRegistry registry = worldguard.getFlagRegistry();
        for (CustomFlag customFlag : CustomFlag.values()) {
            StateFlag flag = new StateFlag(customFlag.getPath(), true);
            try {
                registry.register(flag);
                flags.put(customFlag.getPath(), flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isPvpAllowed(Location loc) {
        return getApplicableRegion(loc).queryState(null, Flags.PVP) != StateFlag.State.DENY;
    }

    @Override
    public boolean isFlagAllowed(Location loc, CustomFlag customFlag) {
        return getApplicableRegion(loc).queryValue(null, flags.get(customFlag.getPath())) != StateFlag.State.DENY;
    }

    @Override
    public boolean isFlagAllowed(Player player, CustomFlag customFlag) {
        return getApplicableRegion(player.getLocation()).queryValue(worldguardPlugin.wrapPlayer(player), flags.get(customFlag.getPath())) != StateFlag.State.DENY;
    }

    private ApplicableRegionSet getApplicableRegion(Location loc) {
        return worldguard.getPlatform().getRegionContainer().createQuery().getApplicableRegions(BukkitAdapter.adapt(loc));
    }
}
